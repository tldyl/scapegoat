package demoMod.scapegoat.patches.events.shrines;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.shrines.FaceTrader;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.relics.GhostMask;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class FaceTraderPatch {
    public static final String ID = Scapegoat.makeID("FaceTrader");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = FaceTrader.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(FaceTrader event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                try {
                    Enum screen = ReflectionHacks.getPrivate(event, FaceTrader.class, "screen");
                    int damage = AbstractDungeon.player.maxHealth / 10;
                    if (damage == 0) {
                        damage = 1;
                    }
                    switch (screen.name()) {
                        case "INTRO":
                            event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                            event.imageEventText.updateDialogOption(0, OPTIONS[0] + damage + OPTIONS[5] + (AbstractDungeon.ascensionLevel >= 15 ? 50 : 75) + OPTIONS[1]);
                            event.imageEventText.setDialogOption(OPTIONS[2]);
                            int amount = AbstractDungeon.ascensionLevel >= 15 ? 2 : 1;
                            event.imageEventText.setDialogOption(String.format(OPTIONS[6], amount, amount), new GhostMask());
                            event.imageEventText.setDialogOption(OPTIONS[3]);
                            ReflectionHacks.setPrivate(event, FaceTrader.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.shrines.FaceTrader$CurScreen"), "MAIN"));
                            return SpireReturn.Return(null);
                        case "MAIN":
                            switch (buttonPressed) {
                                case 2:
                                    AbstractRelic r = new GhostMask();
                                    AbstractEvent.logMetricObtainRelic("FaceTrader", "Trade", r);
                                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, r);
                                    SinAndBloodstainManager.increaseSin(1);
                                    SinAndBloodstainManager.increaseBloodstain(1);
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                                    event.imageEventText.clearAllDialogs();
                                    event.imageEventText.setDialogOption(OPTIONS[3]);
                                    ReflectionHacks.setPrivate(event, FaceTrader.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.shrines.FaceTrader$CurScreen"), "RESULT"));
                                    return SpireReturn.Return(null);
                                case 3:
                                    event.logMetric("Leave");
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                                    event.imageEventText.clearAllDialogs();
                                    event.imageEventText.setDialogOption(OPTIONS[3]);
                                    ReflectionHacks.setPrivate(event, FaceTrader.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.shrines.FaceTrader$CurScreen"), "RESULT"));
                                    return SpireReturn.Return(null);
                            }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return SpireReturn.Continue();
        }
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}

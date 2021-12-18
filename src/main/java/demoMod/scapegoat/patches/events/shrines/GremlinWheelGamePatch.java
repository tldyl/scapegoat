package demoMod.scapegoat.patches.events.shrines;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class GremlinWheelGamePatch {
    public static final String ID = Scapegoat.makeID("Wheel of Change");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = GremlinWheelGame.class,
            method = "preApplyResult"
    )
    public static class PatchPreApplyResult {
        public static void Postfix(GremlinWheelGame event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.setDialogOption(String.format(OPTIONS[10], AbstractDungeon.ascensionLevel >= 15 ? 2 : 1));
            }
        }
    }

    @SpirePatch(
            clz = GremlinWheelGame.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(GremlinWheelGame event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                Enum screen = ReflectionHacks.getPrivate(event, GremlinWheelGame.class, "screen");
                switch (screen.name()) {
                    case "COMPLETE":
                        switch (buttonPressed) {
                            case 1:
                                event.imageEventText.updateBodyText(DESCRIPTIONS[8]);
                                event.imageEventText.clearAllDialogs();
                                event.imageEventText.setDialogOption(OPTIONS[8]);
                                AbstractDungeon.player.increaseMaxHp(9, true);
                                SinAndBloodstainManager.increaseSin(1);
                                try {
                                    ReflectionHacks.setPrivate(event, GremlinWheelGame.class, "screen", Enum.valueOf((Class<Enum>)Class.forName("com.megacrit.cardcrawl.events.shrines.GremlinWheelGame$CUR_SCREEN"), "LEAVE"));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                return SpireReturn.Return(null);
                        }
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

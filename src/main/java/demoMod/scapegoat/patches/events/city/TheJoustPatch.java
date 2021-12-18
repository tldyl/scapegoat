package demoMod.scapegoat.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.TheJoust;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class TheJoustPatch {
    public static final String ID = Scapegoat.makeID("The Joust");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = TheJoust.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(TheJoust event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                Enum screen = ReflectionHacks.getPrivate(event, TheJoust.class, "screen");
                switch (screen.name()) {
                    case "HALT":
                        event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        event.imageEventText.updateDialogOption(0, OPTIONS[1] + 50 + OPTIONS[2] + 100 + OPTIONS[3]);
                        event.imageEventText.setDialogOption(OPTIONS[4] + 50 + OPTIONS[5] + 250 + OPTIONS[3]);
                        try {
                            ReflectionHacks.setPrivate(event, TheJoust.class, "screen", Enum.valueOf((Class<Enum>)Class.forName("com.megacrit.cardcrawl.events.city.TheJoust$CUR_SCREEN"), "EXPLANATION"));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        int ascensionLevel = AbstractDungeon.ascensionLevel;
                        event.imageEventText.setDialogOption(String.format(OPTIONS[8], ascensionLevel >= 15 ? 3 : 2, ascensionLevel >= 15 ? 2 : 1));
                        return SpireReturn.Return(null);
                    case "EXPLANATION":
                        switch (buttonPressed) {
                            case 2:
                                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, false);
                                CardCrawlGame.sound.play("ATTACK_HEAVY");
                                event.imageEventText.updateBodyText(DESCRIPTIONS[9]);
                                AbstractDungeon.player.gainGold(300);
                                SinAndBloodstainManager.increaseSin(1);
                                SinAndBloodstainManager.increaseBloodstain(2);
                                try {
                                    ReflectionHacks.setPrivate(event, TheJoust.class, "screen", Enum.valueOf((Class<Enum>)Class.forName("com.megacrit.cardcrawl.events.city.TheJoust$CUR_SCREEN"), "COMPLETE"));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                event.imageEventText.updateDialogOption(0, OPTIONS[7]);
                                event.imageEventText.clearRemainingOptions();
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

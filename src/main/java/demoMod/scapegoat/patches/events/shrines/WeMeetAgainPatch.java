package demoMod.scapegoat.patches.events.shrines;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class WeMeetAgainPatch {
    public static final String ID = Scapegoat.makeID("WeMeetAgain");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = WeMeetAgain.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(WeMeetAgain event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.updateDialogOption(3, String.format(OPTIONS[7], AbstractDungeon.ascensionLevel >= 15 ? 2 : 1));
            }
        }
    }

    @SpirePatch(
            clz = WeMeetAgain.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(WeMeetAgain event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                Enum screen = ReflectionHacks.getPrivate(event, WeMeetAgain.class, "screen");
                switch (screen.name()) {
                    case "INTRO":
                        event.imageEventText.updateBodyText(DESCRIPTIONS[6]);
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
                        CardCrawlGame.sound.play("BLUNT_HEAVY");
                        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)Settings.WIDTH * 0.28F, (float)Settings.HEIGHT / 2.0F, relic);
                        SinAndBloodstainManager.increaseSin(1);
                        AbstractEvent.logMetricIgnored("WeMeetAgain");
                        event.imageEventText.updateDialogOption(0, OPTIONS[8]);
                        event.imageEventText.clearRemainingOptions();
                        try {
                            ReflectionHacks.setPrivate(event, WeMeetAgain.class, "screen", Enum.valueOf((Class<Enum>)Class.forName("com.megacrit.cardcrawl.events.shrines.WeMeetAgain$CUR_SCREEN"), "COMPLETE"));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        return SpireReturn.Return(null);
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

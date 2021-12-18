package demoMod.scapegoat.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.Ghosts;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.relics.RuneEctoplasm;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class GhostsPatch {
    public static final String ID = Scapegoat.makeID("Ghosts");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = Ghosts.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(Ghosts event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.updateDialogOption(0, OPTIONS[0] + (int) (AbstractDungeon.player.maxHealth * 0.4) + String.format(OPTIONS[1], AbstractDungeon.ascensionLevel >= 15 ? 2 : 1));
                ReflectionHacks.setPrivate(event.imageEventText.optionList.get(0), LargeDialogOptionButton.class,"relicToPreview", new RuneEctoplasm());
            }
        }
    }

    @SpirePatch(
            clz = Ghosts.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Ghosts event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, Ghosts.class, "screenNum");
                switch (screenNum) {
                    case 0:
                        switch (buttonPressed) {
                            case 0:
                                event.imageEventText.updateBodyText(DESCRIPTIONS[1] + DESCRIPTIONS[2]);
                                AbstractDungeon.player.decreaseMaxHealth((int) (AbstractDungeon.player.maxHealth * 0.4));
                                ReflectionHacks.setPrivate(event, Ghosts.class, "screenNum", 1);
                                new RuneEctoplasm().instantObtain();
                                SinAndBloodstainManager.increaseBloodstain(1);
                                event.imageEventText.updateDialogOption(0, OPTIONS[3]);
                                event.imageEventText.clearRemainingOptions();
                                return SpireReturn.Return(null);
                        }
                        AbstractEvent.logMetricIgnored("Ghosts");
                        event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        ReflectionHacks.setPrivate(event, Ghosts.class, "screenNum", 2);
                        event.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        event.imageEventText.clearRemainingOptions();
                        return SpireReturn.Return(null);
                    case 1:

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

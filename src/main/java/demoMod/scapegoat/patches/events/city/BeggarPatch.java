package demoMod.scapegoat.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.Beggar;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class BeggarPatch {
    public static final String ID = Scapegoat.makeID("Beggar");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private static boolean discovered;

    @SpirePatch(
            clz = Beggar.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(Beggar event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                discovered = false;
                if (AbstractDungeon.player.gold >= 50) {
                    event.imageEventText.optionList.add(1, new LargeDialogOptionButton(1, String.format(OPTIONS[7], AbstractDungeon.ascensionLevel >= 15 ? 2 : 1)));
                } else {
                    event.imageEventText.optionList.add(1, new LargeDialogOptionButton(1, OPTIONS[2] + 50 + OPTIONS[3], true));
                }
                event.imageEventText.optionList.get(2).slot = 2;
                for (LargeDialogOptionButton button : event.imageEventText.optionList) {
                    button.calculateY(event.imageEventText.optionList.size());
                }
            }
        }
    }

    @SpirePatch(
            clz = Beggar.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Beggar event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                Enum screen = ReflectionHacks.getPrivate(event, Beggar.class, "screen");
                switch (screen.name()) {
                    case "INTRO":
                        switch (buttonPressed) {
                            case 0:
                                event.imageEventText.loadImage("images/events/cleric.jpg");
                                event.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                                AbstractDungeon.player.loseGold(75);
                                event.imageEventText.clearAllDialogs();
                                event.imageEventText.setDialogOption(OPTIONS[4]);
                                ReflectionHacks.setPrivate(event, Beggar.class, "screen", Beggar.CurScreen.GAVE_MONEY);
                                return SpireReturn.Return(null);
                            case 1:
                                event.imageEventText.loadImage("images/events/cleric.jpg");
                                event.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                                AbstractDungeon.player.loseGold(50);
                                event.imageEventText.clearAllDialogs();
                                event.imageEventText.setDialogOption(OPTIONS[4]);
                                discovered = true;
                                SinAndBloodstainManager.increaseSin(1);
                                ReflectionHacks.setPrivate(event, Beggar.class, "screen", Beggar.CurScreen.GAVE_MONEY);
                                return SpireReturn.Return(null);
                            case 2:
                                event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                                event.imageEventText.clearAllDialogs();
                                event.imageEventText.setDialogOption(OPTIONS[5]);
                                ReflectionHacks.setPrivate(event, Beggar.class, "screen", Beggar.CurScreen.LEAVE);
                                AbstractEvent.logMetricIgnored("Beggar");
                                return SpireReturn.Return(null);
                        }
                    case "GAVE_MONEY":
                        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[6], false, false, false, true);
                        event.imageEventText.clearAllDialogs();
                        event.imageEventText.setDialogOption(OPTIONS[5]);
                        ReflectionHacks.setPrivate(event, Beggar.class, "screen", Beggar.CurScreen.LEAVE);
                        if (discovered) {
                            event.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        } else {
                            event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
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

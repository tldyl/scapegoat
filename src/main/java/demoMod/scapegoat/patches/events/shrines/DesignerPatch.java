package demoMod.scapegoat.patches.events.shrines;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.shrines.Designer;
import com.megacrit.cardcrawl.localization.EventStrings;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.relics.TechInSpire;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class DesignerPatch {
    public static final String ID = Scapegoat.makeID("Designer");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = Designer.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Designer event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                try {
                    Enum screen = ReflectionHacks.getPrivate(event, Designer.class, "curScreen");
                    boolean adjustmentUpgradesOne = ReflectionHacks.getPrivate(event, Designer.class, "adjustmentUpgradesOne");
                    boolean cleanUpRemovesCards = ReflectionHacks.getPrivate(event, Designer.class, "cleanUpRemovesCards");
                    int adjustCost = ReflectionHacks.getPrivate(event, Designer.class, "adjustCost");
                    int cleanUpCost = ReflectionHacks.getPrivate(event, Designer.class, "cleanUpCost");
                    int fullServiceCost = ReflectionHacks.getPrivate(event, Designer.class, "fullServiceCost");
                    switch (screen.name()) {
                        case "INTRO":
                            event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                            event.imageEventText.removeDialogOption(0);

                            if (adjustmentUpgradesOne) {
                                event.imageEventText.updateDialogOption(0, OPTIONS[1] + adjustCost + OPTIONS[6] + OPTIONS[9], (AbstractDungeon.player.gold < adjustCost || !AbstractDungeon.player.masterDeck.hasUpgradableCards()));
                            } else {
                                event.imageEventText.updateDialogOption(0, OPTIONS[1] + adjustCost + OPTIONS[6] + OPTIONS[7] + 2 + OPTIONS[8], (AbstractDungeon.player.gold < adjustCost || !AbstractDungeon.player.masterDeck.hasUpgradableCards()));
                            }

                            if (cleanUpRemovesCards) {
                                event.imageEventText.setDialogOption(OPTIONS[2] + cleanUpCost + OPTIONS[6] + OPTIONS[10], (AbstractDungeon.player.gold < cleanUpCost || CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).size() == 0));
                            } else {
                                event.imageEventText.setDialogOption(OPTIONS[2] + cleanUpCost + OPTIONS[6] + OPTIONS[11] + 2 + OPTIONS[12], (AbstractDungeon.player.gold < cleanUpCost || CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).size() < 2));
                            }

                            event.imageEventText.setDialogOption(OPTIONS[3] + fullServiceCost + OPTIONS[6] + OPTIONS[13], (AbstractDungeon.player.gold < fullServiceCost || CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).size() == 0));
                            event.imageEventText.setDialogOption(String.format(OPTIONS[18], AbstractDungeon.ascensionLevel >= 15 ? 2 : 1), new TechInSpire());

                            ReflectionHacks.setPrivate(event, Designer.class, "curScreen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.shrines.Designer$CurrentScreen"), "MAIN"));
                            return SpireReturn.Return(null);
                        case "MAIN":
                            switch (buttonPressed) {
                                case 3:
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                                    AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
                                    SinAndBloodstainManager.increaseBloodstain(1);
                                    float drawX = ReflectionHacks.getPrivate(event, AbstractEvent.class, "drawX");
                                    float drawY = ReflectionHacks.getPrivate(event, AbstractEvent.class, "drawY");
                                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, new TechInSpire());
                                    event.imageEventText.updateDialogOption(0, OPTIONS[14]);
                                    event.imageEventText.clearRemainingOptions();
                                    ReflectionHacks.setPrivate(event, Designer.class, "curScreen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.shrines.Designer$CurrentScreen"), "DONE"));
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

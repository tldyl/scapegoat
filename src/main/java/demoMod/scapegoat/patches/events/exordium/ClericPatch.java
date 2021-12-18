package demoMod.scapegoat.patches.events.exordium;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.exordium.Cleric;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class ClericPatch {
    public static final String ID = Scapegoat.makeID("The Cleric");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = Cleric.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(Cleric event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                ReflectionHacks.setPrivate(event, AbstractEvent.class, "body", DESCRIPTIONS[0]);
                event.imageEventText.updateBodyText(DESCRIPTIONS[0]);
                event.imageEventText.clearAllDialogs();
                event.imageEventText.setDialogOption(String.format(OPTIONS[0], AbstractDungeon.ascensionLevel >= 15 ? 2 : 1));
                event.imageEventText.setDialogOption(OPTIONS[1]);
            }
        }
    }

    @SpirePatch(
            clz = Cleric.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Cleric event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, AbstractEvent.class, "screenNum");
                switch (screenNum) {
                    case 0:
                        switch (buttonPressed) {
                            case 0:
                                if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                                    AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[3], false, false, false, true);
                                    SinAndBloodstainManager.increaseSin(1);
                                }
                                event.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                                event.imageEventText.updateDialogOption(0, OPTIONS[2]);
                                event.imageEventText.clearRemainingOptions();
                                ReflectionHacks.setPrivate(event, AbstractEvent.class, "screenNum", 99);
                                return SpireReturn.Return(null);
                            case 1:
                                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, true);
                                CardCrawlGame.sound.play("ATTACK_FIRE");
                                AbstractDungeon.player.damage(new DamageInfo(null, 4));
                                event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                                event.imageEventText.updateDialogOption(0, OPTIONS[2]);
                                event.imageEventText.clearRemainingOptions();
                                ReflectionHacks.setPrivate(event, AbstractEvent.class, "screenNum", 99);
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

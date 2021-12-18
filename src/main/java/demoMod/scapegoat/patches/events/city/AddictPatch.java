package demoMod.scapegoat.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.Addict;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;

public class AddictPatch {
    public static final String ID = Scapegoat.makeID("Addict");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = Addict.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(Addict event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                LargeDialogOptionButton button = new LargeDialogOptionButton(1, OPTIONS[4], new Shame());
                event.imageEventText.optionList.set(1, button);
                button.calculateY(event.imageEventText.optionList.size());
            }
        }
    }

    @SpirePatch(
            clz = Addict.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Addict event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, Addict.class, "screenNum");
                switch (screenNum) {
                    case 0:
                        switch (buttonPressed) {
                            case 1:
                                event.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                                AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                                AbstractEvent.logMetricObtainCardAndRelic("Addict", "Stole Relic", new Shame(), relic);
                                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Shame(), Settings.WIDTH / 2, Settings.HEIGHT / 2));
                                float drawX = ReflectionHacks.getPrivate(event, AbstractEvent.class, "drawX");
                                float drawY = ReflectionHacks.getPrivate(event, AbstractEvent.class, "drawY");
                                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, relic);

                                event.imageEventText.updateDialogOption(0, OPTIONS[5]);
                                event.imageEventText.clearRemainingOptions();
                                ReflectionHacks.setPrivate(event, Addict.class, "screenNum", 1);
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

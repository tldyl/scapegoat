package demoMod.scapegoat.patches.events.exordium;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.exordium.BigFish;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class BigFishPatch {
    public static final String ID = Scapegoat.makeID("Big Fish");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = BigFish.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(BigFish event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.updateDialogOption(2, OPTIONS[4], CardLibrary.getCopy("Regret"));
                event.imageEventText.setDialogOption(String.format(OPTIONS[6], (int) Math.ceil(AbstractDungeon.player.maxHealth * 0.3F), AbstractDungeon.ascensionLevel >= 15 ? 2 : 1));
            }
        }
    }

    @SpirePatch(
            clz = BigFish.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(BigFish event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                try {
                    Enum screen = ReflectionHacks.getPrivate(event, BigFish.class, "screen");
                    switch (screen.name()) {
                        case "INTRO":
                            switch (buttonPressed) {
                                case 2:
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[4] + DESCRIPTIONS[5]);
                                    AbstractCard c = new Regret();
                                    AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                                    AbstractEvent.logMetricObtainCardAndRelic("Big Fish", "Box", c, r);
                                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(CardLibrary.getCopy(c.cardID), (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), r);
                                    ReflectionHacks.setPrivate(event, BigFish.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.exordium.BigFish$CurScreen"), "RESULT"));
                                    event.imageEventText.clearAllDialogs();
                                    event.imageEventText.setDialogOption(OPTIONS[5]);
                                    return SpireReturn.Return(null);
                                case 3:
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[6]);
                                    AbstractDungeon.player.heal((int) Math.ceil(AbstractDungeon.player.maxHealth * 0.3F));
                                    AbstractDungeon.player.increaseMaxHp(5, true);
                                    AbstractEvent.logMetricHeal("Big Fish", "Banana", (int) Math.ceil(AbstractDungeon.player.maxHealth * 0.3F));
                                    AbstractEvent.logMetricMaxHPGain("Big Fish", "Donut", 5);
                                    ReflectionHacks.setPrivate(event, BigFish.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.exordium.BigFish$CurScreen"), "RESULT"));
                                    SinAndBloodstainManager.increaseSin(1);
                                    event.imageEventText.clearAllDialogs();
                                    event.imageEventText.setDialogOption(OPTIONS[5]);
                                    return SpireReturn.Return(null);
                            }
                            break;
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

package demoMod.scapegoat.patches.events.exordium;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.exordium.Sssserpent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.cards.scapegoat.SoulFlame;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class SssserpentPatch {
    public static final String ID = Scapegoat.makeID("Liars Game");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private static final String DIALOG_1;
    private static final String AGREE_DIALOG;
    private static final String DISAGREE_DIALOG;

    private static int optionIndex = 0;

    @SpirePatch(
            clz = Sssserpent.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        @SpireInsertPatch(rloc = 10)
        public static void Insert(Sssserpent event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.optionList.remove(event.imageEventText.optionList.size() - 1);
                event.imageEventText.setDialogOption(String.format(OPTIONS[0], AbstractDungeon.ascensionLevel >= 15 ? 3 : 2), new SoulFlame());
                optionIndex = event.imageEventText.optionList.size() - 1;
            }
        }
    }

    @SpirePatch(
            clz = Sssserpent.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Sssserpent event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                try {
                    Enum screen = ReflectionHacks.getPrivate(event, Sssserpent.class, "screen");
                    switch (screen.name()) {
                        case "INTRO":
                            if (buttonPressed == optionIndex) {
                                event.imageEventText.updateBodyText(AGREE_DIALOG);
                                event.imageEventText.removeDialogOption(1);
                                event.imageEventText.updateDialogOption(0, OPTIONS[2]);
                                ReflectionHacks.setPrivate(event, Sssserpent.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.exordium.Sssserpent$CUR_SCREEN"), "AGREE"));
                                AbstractEvent.logMetricGainGoldAndCard("Liars Game", "AGREE", new SoulFlame(), 0);
                                return SpireReturn.Return(null);
                            }
                            break;
                        case "AGREE":
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new SoulFlame(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                            event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            event.imageEventText.updateDialogOption(0, OPTIONS[3]);
                            SinAndBloodstainManager.increaseBloodstain(2);
                            ReflectionHacks.setPrivate(event, Sssserpent.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.exordium.Sssserpent$CUR_SCREEN"), "COMPLETE"));
                            return SpireReturn.Return(null);
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
        DIALOG_1 = DESCRIPTIONS[0];
        AGREE_DIALOG = DESCRIPTIONS[1];
        DISAGREE_DIALOG = DESCRIPTIONS[2];
    }
}

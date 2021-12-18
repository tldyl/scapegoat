package demoMod.scapegoat.patches.events.beyond;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.beyond.TombRedMask;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.RedMask;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class TombRedMaskPatch {
    public static final String ID = Scapegoat.makeID("Tomb of Lord Red Mask");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = TombRedMask.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(TombRedMask event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                int slot = event.imageEventText.optionList.size() - 1;
                event.imageEventText.optionList.add(slot, new LargeDialogOptionButton(slot, String.format(OPTIONS[5], AbstractDungeon.ascensionLevel >= 15 ? 2 : 1)));
                event.imageEventText.optionList.get(slot + 1).slot = slot + 1;
                for (LargeDialogOptionButton button : event.imageEventText.optionList) {
                    button.calculateY(slot + 1);
                }
            }
        }
    }

    @SpirePatch(
            clz = TombRedMask.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(TombRedMask event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                Enum screen = ReflectionHacks.getPrivate(event, TombRedMask.class, "screen");
                switch (screen.name()) {
                    case "INTRO":
                        if (buttonPressed == event.imageEventText.optionList.size() - 2) {
                            AbstractRelic relic;
                            if (AbstractDungeon.player.hasRelic(RedMask.ID)) {
                                relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON);
                            } else {
                                relic = new RedMask();
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                            AbstractDungeon.effectList.add(new RainingGoldEffect(100));
                            AbstractDungeon.player.gainGold(100);
                            SinAndBloodstainManager.increaseSin(1);
                            event.imageEventText.updateBodyText(DESCRIPTIONS[3]);

                            event.imageEventText.clearAllDialogs();
                            event.imageEventText.setDialogOption(OPTIONS[4]);
                            try {
                                ReflectionHacks.setPrivate(event, TombRedMask.class, "screen", Enum.valueOf((Class<Enum>)Class.forName("com.megacrit.cardcrawl.events.beyond.TombRedMask$CurScreen"), "RESULT"));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            AbstractEvent.logMetricGainGold("Tomb of Lord Red Mask", "Wore Mask", 100);
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

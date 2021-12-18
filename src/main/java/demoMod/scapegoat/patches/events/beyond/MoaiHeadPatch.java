package demoMod.scapegoat.patches.events.beyond;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.beyond.MoaiHead;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.cards.scapegoat.SelfSacrifice;
import demoMod.scapegoat.characters.ScapegoatCharacter;

public class MoaiHeadPatch {
    public static final String ID = Scapegoat.makeID("The Moai Head");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = MoaiHead.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(MoaiHead event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                LargeDialogOptionButton button = new LargeDialogOptionButton(0, OPTIONS[0] + MathUtils.round(AbstractDungeon.player.maxHealth * 0.1F) + OPTIONS[1], new SelfSacrifice());
                event.imageEventText.optionList.set(0, button);
                button.calculateY(event.imageEventText.optionList.size());
            }
        }
    }

    @SpirePatch(
            clz = MoaiHead.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(MoaiHead event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, MoaiHead.class, "screenNum");
                switch (screenNum) {
                    case 0:
                        switch (buttonPressed) {
                            case 0:
                                event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true);
                                CardCrawlGame.sound.play("BLUNT_HEAVY");
                                AbstractDungeon.player.decreaseMaxHealth(MathUtils.round(AbstractDungeon.player.maxHealth * 0.1F));
                                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new SelfSacrifice(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                                AbstractEvent.logMetricHealAndLoseMaxHP("The Moai Head", "Heal", AbstractDungeon.player.maxHealth, MathUtils.round(AbstractDungeon.player.maxHealth * 0.1F));
                                ReflectionHacks.setPrivate(event, MoaiHead.class, "screenNum", 1);
                                event.imageEventText.updateDialogOption(0, OPTIONS[4]);
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

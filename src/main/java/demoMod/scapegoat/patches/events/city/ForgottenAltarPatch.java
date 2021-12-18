package demoMod.scapegoat.patches.events.city;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.ForgottenAltar;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;

public class ForgottenAltarPatch {
    public static final String ID = Scapegoat.makeID("Forgotten Altar");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = ForgottenAltar.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(ForgottenAltar event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.optionList.set(1, new LargeDialogOptionButton(1, OPTIONS[2] + MathUtils.round(AbstractDungeon.player.maxHealth * 0.1F) + OPTIONS[3]));
            }
        }
    }

    @SpirePatch(
            clz = ForgottenAltar.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(ForgottenAltar event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, AbstractEvent.class, "screenNum");
                switch (screenNum) {
                    case 0:
                        switch (buttonPressed) {
                            case 1:
                                CardCrawlGame.sound.play("DEBUFF_2");
                                AbstractDungeon.player.damage(new DamageInfo(null, MathUtils.round(AbstractDungeon.player.maxHealth * 0.1F)));
                                event.showProceedScreen(DESCRIPTIONS[2]);
                                AbstractEvent.logMetricTakeDamage("Forgotten Altar", "Smashed Altar", MathUtils.round(AbstractDungeon.player.maxHealth * 0.1F));
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

package demoMod.scapegoat.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.interfaces.OnPlayerDeathCard;
import demoMod.scapegoat.interfaces.PreDamageSubscriber;
import javassist.CtBehavior;

@SuppressWarnings("unused")
public class AbstractPlayerPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class PatchDamage {
        @SpireInsertPatch(rloc = 1)
        public static void Insert1(AbstractPlayer p, DamageInfo info) {
            for (AbstractPower power : p.powers) {
                if (power instanceof PreDamageSubscriber) {
                    ((PreDamageSubscriber) power).justAttacked(info);
                }
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> Insert2(AbstractPlayer p, DamageInfo info) {
            for (AbstractCard card : p.hand.group) {
                if (card instanceof OnPlayerDeathCard) {
                    if (!((OnPlayerDeathCard) card).onPlayerDeath(p, info)) {
                        return SpireReturn.Return(null);
                    }
                }
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isDead");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}

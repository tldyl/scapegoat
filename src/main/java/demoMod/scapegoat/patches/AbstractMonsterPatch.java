package demoMod.scapegoat.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import demoMod.scapegoat.powers.SpiritLinkPower;

public class AbstractMonsterPatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class PatchDamage {
        public static void Prefix(AbstractMonster m, DamageInfo info) {
            if (info.output > 0 && m.hasPower(IntangiblePower.POWER_ID)) {
                info.output = 1;
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez = {
                    boolean.class
            }
    )
    public static class PatchDie {
        public static void Prefix(AbstractMonster m, boolean triggerRelics) {
            if (!m.isDying && m.currentHealth <= 0) {
                if (m.hasPower(SpiritLinkPower.POWER_ID)) {
                    AbstractPower power = m.getPower(SpiritLinkPower.POWER_ID);
                    power.onDeath();
                }
            }
        }
    }
}

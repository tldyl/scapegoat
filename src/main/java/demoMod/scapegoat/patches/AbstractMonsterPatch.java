package demoMod.scapegoat.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;

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
}

package demoMod.scapegoat.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import demoMod.scapegoat.powers.AimingShotPower;

public class MonsterGroupPatch {
    @SpirePatch(
            clz = MonsterGroup.class,
            method = "getRandomMonster",
            paramtypez = {
                    AbstractMonster.class,
                    boolean.class,
                    Random.class
            }
    )
    @SpirePatch(
            clz = MonsterGroup.class,
            method = "getRandomMonster",
            paramtypez = {
                    AbstractMonster.class,
                    boolean.class
            }
    )
    public static class PatchGetRandomMonster {
        public static SpireReturn<AbstractMonster> Prefix(MonsterGroup group) {
            for (AbstractMonster monster : group.monsters) {
                if (!monster.isDeadOrEscaped() && monster.hasPower(AimingShotPower.POWER_ID)) {
                    return SpireReturn.Return(monster);
                }
            }
            return SpireReturn.Continue();
        }
    }
}

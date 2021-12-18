package demoMod.scapegoat.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GameActionManagerPatch {
    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class PatchGetNextAction {
        public static void Postfix(GameActionManager actionManager) {
            if (actionManager.turnHasEnded && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AddFieldPatch.totalBurialThisTurn.set(actionManager, 0);
            }
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class PatchClear {
        public static void Postfix(GameActionManager actionManager) {
            AddFieldPatch.totalBurialThisTurn.set(actionManager, 0);
            AddFieldPatch.paybackGained.set(actionManager, 0);
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = SpirePatch.CLASS
    )
    public static class AddFieldPatch {
        public static SpireField<Integer> totalBurialThisTurn = new SpireField<>(() -> 0);
        public static SpireField<Integer> paybackGained = new SpireField<>(() -> 0);
    }
}

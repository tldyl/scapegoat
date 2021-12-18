package demoMod.scapegoat.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.ArrayList;
import java.util.List;

public class ShuffleActionPatch {
    @SpirePatch(
            clz = ShuffleAction.class,
            method = "update"
    )
    public static class PatchUpdate {
        @SpireInsertPatch(rloc = 6)
        public static void Insert(ShuffleAction action) {
            CardGroup group = ReflectionHacks.getPrivate(action, ShuffleAction.class, "group");
            List<AbstractCard> tmp = new ArrayList<>();
            for (AbstractCard card : group.group) {
                if (AbstractCardPatch.AddFieldPatch.isBottom.get(card)) {
                    tmp.add(card);
                }
            }
            group.group.removeAll(tmp);
            group.group.addAll(0, tmp);
        }
    }
}

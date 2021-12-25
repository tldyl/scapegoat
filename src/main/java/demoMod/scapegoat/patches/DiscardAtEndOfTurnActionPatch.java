package demoMod.scapegoat.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.scapegoat.cards.scapegoat.CurtainCall;

public class DiscardAtEndOfTurnActionPatch {
    @SpirePatch(
            clz = DiscardAtEndOfTurnAction.class,
            method = "update"
    )
    public static class PatchUpdate {
        public static void Postfix(DiscardAtEndOfTurnAction action) {
            if (action.isDone) {
                AbstractPlayer p = AbstractDungeon.player;
                for (AbstractCard card : p.discardPile.group) {
                    if (card instanceof CurtainCall) {
                        card.triggerOnEndOfPlayerTurn();
                    }
                }
                for (AbstractCard card : p.drawPile.group) {
                    if (card instanceof CurtainCall) {
                        card.triggerOnEndOfPlayerTurn();
                    }
                }
                for (AbstractCard card : p.exhaustPile.group) {
                    if (card instanceof CurtainCall) {
                        card.triggerOnEndOfPlayerTurn();
                    }
                }
            }
        }
    }
}

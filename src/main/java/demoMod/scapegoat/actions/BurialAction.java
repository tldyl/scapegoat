package demoMod.scapegoat.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.interfaces.PostBurialSubscriber;
import demoMod.scapegoat.patches.GameActionManagerPatch;

public class BurialAction extends AbstractGameAction {
    public BurialAction(int amount) {
        this.amount = amount;
        this.duration = this.startDuration = AbstractGameAction.DEFAULT_DURATION;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            for (int i=0;i<this.amount;i++) {
                if (AbstractDungeon.player.drawPile.group.size() == 0) {
                    break;
                }
                AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
                AbstractDungeon.player.drawPile.moveToDiscardPile(card);
                if (card instanceof PostBurialSubscriber) {
                    ((PostBurialSubscriber) card).onBurial();
                }
                GameActionManagerPatch.AddFieldPatch.totalBurialThisTurn.set(AbstractDungeon.actionManager, GameActionManagerPatch.AddFieldPatch.totalBurialThisTurn.get(AbstractDungeon.actionManager) + 1);
                for (AbstractPower power : AbstractDungeon.player.powers) {
                    if (power instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) power).onBurial(card);
                    }
                }

                for (AbstractCard card1 : AbstractDungeon.player.drawPile.group) {
                    if (card1 instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) card1).onBurial(card);
                    }
                }
                for (AbstractCard card1 : AbstractDungeon.player.hand.group) {
                    if (card1 instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) card1).onBurial(card);
                    }
                }
                for (AbstractCard card1 : AbstractDungeon.player.discardPile.group) {
                    if (card1 instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) card1).onBurial(card);
                    }
                }
                for (AbstractCard card1 : AbstractDungeon.player.exhaustPile.group) {
                    if (card1 instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) card1).onBurial(card);
                    }
                }
            }
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof PostBurialSubscriber) {
                    ((PostBurialSubscriber) power).onBurial();
                }
            }

            for (AbstractCard card1 : AbstractDungeon.player.drawPile.group) {
                if (card1 instanceof PostBurialSubscriber) {
                    ((PostBurialSubscriber) card1).onBurialForCard();
                }
            }
            for (AbstractCard card1 : AbstractDungeon.player.hand.group) {
                if (card1 instanceof PostBurialSubscriber) {
                    ((PostBurialSubscriber) card1).onBurialForCard();
                }
            }
            for (AbstractCard card1 : AbstractDungeon.player.discardPile.group) {
                if (card1 instanceof PostBurialSubscriber) {
                    ((PostBurialSubscriber) card1).onBurialForCard();
                }
            }
            for (AbstractCard card1 : AbstractDungeon.player.exhaustPile.group) {
                if (card1 instanceof PostBurialSubscriber) {
                    ((PostBurialSubscriber) card1).onBurialForCard();
                }
            }
        }
        tickDuration();
    }
}

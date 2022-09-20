package demoMod.scapegoat.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.defect.RecycleAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MaliciousErosionAction extends AbstractGameAction {
    private AbstractPlayer p;

    public MaliciousErosionAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        if (!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            this.target = AbstractDungeon.getRandomMonster();
        }
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
            } else if (this.p.hand.size() == 1) {
                if (this.p.hand.getBottomCard().costForTurn > 0) {
                    this.addToTop(new DrawCardAction(this.p.hand.getBottomCard().costForTurn));
                }
                playAndExhaustCard(this.p.hand.getBottomCard());
                this.tickDuration();
            } else {
                AbstractDungeon.handCardSelectScreen.open(RecycleAction.TEXT[0], 1, false);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                for(AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    if (c.costForTurn > 0) {
                        this.addToTop(new DrawCardAction(c.costForTurn));
                    }
                    playAndExhaustCard(c);
                }

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }

            this.tickDuration();
        }
    }

    private void playAndExhaustCard(AbstractCard card) {
        AbstractDungeon.player.drawPile.group.remove(card);
        AbstractDungeon.getCurrRoom().souls.remove(card);
        AbstractDungeon.player.limbo.group.add(card);
        card.current_y = -200.0F * Settings.scale;
        card.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
        card.target_y = (float)Settings.HEIGHT / 2.0F;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        card.applyPowers();
        card.exhaustOnUseOnce = true;
        this.addToTop(new NewQueueCardAction(card, this.target, false, true));
        this.addToTop(new UnlimboAction(card));
        if (!Settings.FAST_MODE) {
            this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        } else {
            this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
        }
    }
}

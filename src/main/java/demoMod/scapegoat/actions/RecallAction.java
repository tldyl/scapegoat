package demoMod.scapegoat.actions;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.BetterDiscardPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.interfaces.PostRecallSubscriber;
import demoMod.scapegoat.relics.BurningEye;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RecallAction extends AbstractGameAction {
    private AbstractPlayer player;
    private CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private boolean setFreeToPlayOnce = false;
    private AbstractGameAction followupAction;
    public static List<AbstractCard> lastSelectedCard = new ArrayList<>();

    public RecallAction(int amount) {
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
    }

    public RecallAction(int amount, boolean setFreeToPlayOnce) {
        this(amount);
        this.setFreeToPlayOnce = setFreeToPlayOnce;
    }

    public RecallAction(int amount, AbstractGameAction followupAction) {
        this(amount);
        this.followupAction = followupAction;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            lastSelectedCard.clear();
            if (player.hasRelic(BurningEye.ID)) {
                this.amount += 2;
            }
            if (!this.player.discardPile.isEmpty() && this.amount > 0 && !(this.player.hand.size() >= Settings.MAX_HAND_SIZE)) {
                List<AbstractCard> tmpList = new ArrayList<>();
                List<AbstractCard> cpyList = new ArrayList<>(this.player.discardPile.group);
                if (this.amount >= this.player.discardPile.size()) {
                    tmpList.addAll(this.player.discardPile.group);
                } else {
                    for (int i=0;i<this.amount;i++) {
                        tmpList.add(cpyList.remove(AbstractDungeon.cardRng.random(cpyList.size() - 1)));
                    }
                }
                tmpGroup.group.addAll(tmpList);
                if (player.hasRelic(BurningEye.ID)) {
                    this.amount -= 2;
                }
                AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, BetterDiscardPileToHandAction.TEXT[1] + this.amount + BetterDiscardPileToHandAction.TEXT[2]);
            }
            this.tickDuration();
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard selectedCard : AbstractDungeon.gridSelectScreen.selectedCards) {
                    if (this.player.hand.size() < 10) {
                        if (this.setFreeToPlayOnce) {
                            selectedCard.setCostForTurn(0);
                        }
                        this.player.hand.addToHand(selectedCard);
                        this.player.discardPile.removeCard(selectedCard);
                    }
                    lastSelectedCard.add(selectedCard);
                    selectedCard.lighten(false);
                    selectedCard.unhover();
                    selectedCard.applyPowers();
                }

                for (AbstractCard card : this.player.discardPile.group) {
                    card.unhover();
                    card.target_x = (float)CardGroup.DISCARD_PILE_X;
                    card.target_y = 0.0F;
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
            if (this.isDone) {
                for (AbstractCard card : this.player.hand.group) {
                    card.applyPowers();
                }
                if (this.followupAction != null) {
                    addToTop(followupAction);
                }
                boolean modLoaded = Loader.isModLoaded("DerFreischutz");
                Class<?> recall = null;
                try {
                    if (modLoaded) {
                        recall = Class.forName("demoMod.derfreischutz.interfaces.PostRecallSubscriber");
                    }
                    for (AbstractPower power : AbstractDungeon.player.powers) {
                        if (power instanceof PostRecallSubscriber) {
                            ((PostRecallSubscriber) power).onRecall();
                        }
                        if (modLoaded) {
                            if (recall.isInstance(power)) {
                                Method method = recall.getDeclaredMethod("onRecall");
                                method.invoke(power);
                            }
                        }
                    }
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

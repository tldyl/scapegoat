package demoMod.scapegoat.actions;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.interfaces.PostBurialSubscriber;
import demoMod.scapegoat.patches.GameActionManagerPatch;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BurialAction extends AbstractGameAction {
    public BurialAction(int amount) {
        this.amount = amount;
        this.duration = this.startDuration = 1.5F;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        try {
            if (this.duration == this.startDuration) {
                boolean modLoaded = Loader.isModLoaded("DerFreischutz");
                Class<?> burial = null;
                Class<?> totalBurialThisTurn;
                Field field = null;
                if (modLoaded) {
                    burial = Class.forName("demoMod.derfreischutz.interfaces.PostBurialSubscriber");
                    totalBurialThisTurn = Class.forName("demoMod.derfreischutz.patches.GameActionManagerPatch$AddFieldPatch");
                    field = totalBurialThisTurn.getDeclaredField("totalBurialThisTurn");
                }
                for (int i=0;i<this.amount;i++) {
                    if (AbstractDungeon.player.drawPile.group.size() == 0) {
                        if (AbstractDungeon.player.hasRelic("IsaacExt:Birthright") && AbstractDungeon.player.discardPile.group.size() > 0 && AbstractDungeon.player instanceof ScapegoatCharacter) {
                            AbstractDungeon.player.getRelic("IsaacExt:Birthright").onTrigger();
                            addToBot(new EmptyDeckShuffleAction());
                            addToBot(new BurialAction(this.amount - i));
                        }
                        break;
                    }
                    AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
                    AbstractDungeon.player.drawPile.group.remove(card);
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card));
                    if (card instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) card).onBurial();
                    }
                    if (modLoaded) {
                        if (burial.isInstance(card)) {
                            Method method = burial.getDeclaredMethod("onBurial");
                            method.invoke(card);
                        }
                    }
                    GameActionManagerPatch.AddFieldPatch.totalBurialThisTurn.set(AbstractDungeon.actionManager, GameActionManagerPatch.AddFieldPatch.totalBurialThisTurn.get(AbstractDungeon.actionManager) + 1);
                    if (modLoaded) {
                        SpireField<Integer> spireField = (SpireField<Integer>) field.get(null);
                        spireField.set(AbstractDungeon.actionManager, spireField.get(AbstractDungeon.actionManager) + 1);
                    }
                    for (AbstractPower power : AbstractDungeon.player.powers) {
                        if (power instanceof PostBurialSubscriber) {
                            ((PostBurialSubscriber) power).onBurial(card);
                        }
                        if (modLoaded) {
                            if (burial.isInstance(power)) {
                                Method method = burial.getDeclaredMethod("onBurial", AbstractCard.class);
                                method.invoke(power, card);
                            }
                        }
                    }

                    for (AbstractCard card1 : AbstractDungeon.player.drawPile.group) {
                        if (card1 instanceof PostBurialSubscriber) {
                            ((PostBurialSubscriber) card1).onBurial(card);
                        }
                        if (modLoaded) {
                            if (burial.isInstance(card1)) {
                                Method method = burial.getDeclaredMethod("onBurial", AbstractCard.class);
                                method.invoke(card1, card);
                            }
                        }
                    }
                    for (AbstractCard card1 : AbstractDungeon.player.hand.group) {
                        if (card1 instanceof PostBurialSubscriber) {
                            ((PostBurialSubscriber) card1).onBurial(card);
                        }
                        if (modLoaded) {
                            if (burial.isInstance(card1)) {
                                Method method = burial.getDeclaredMethod("onBurial", AbstractCard.class);
                                method.invoke(card1, card);
                            }
                        }
                    }
                    for (AbstractCard card1 : AbstractDungeon.player.discardPile.group) {
                        if (card1 instanceof PostBurialSubscriber) {
                            ((PostBurialSubscriber) card1).onBurial(card);
                        }
                        if (modLoaded) {
                            if (burial.isInstance(card1)) {
                                Method method = burial.getDeclaredMethod("onBurial", AbstractCard.class);
                                method.invoke(card1, card);
                            }
                        }
                    }
                    for (AbstractCard card1 : AbstractDungeon.player.exhaustPile.group) {
                        if (card1 instanceof PostBurialSubscriber) {
                            ((PostBurialSubscriber) card1).onBurial(card);
                        }
                        if (modLoaded) {
                            if (burial.isInstance(card1)) {
                                Method method = burial.getDeclaredMethod("onBurial", AbstractCard.class);
                                method.invoke(card1, card);
                            }
                        }
                    }
                }
                for (AbstractPower power : AbstractDungeon.player.powers) {
                    if (power instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) power).onBurial();
                    }
                    if (modLoaded) {
                        if (burial.isInstance(power)) {
                            Method method = burial.getDeclaredMethod("onBurial");
                            method.invoke(power);
                        }
                    }
                }

                for (AbstractCard card1 : AbstractDungeon.player.drawPile.group) {
                    if (card1 instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) card1).onBurialForCard();
                    }
                    if (modLoaded) {
                        if (burial.isInstance(card1)) {
                            Method method = burial.getDeclaredMethod("onBurialForCard");
                            method.invoke(card1);
                        }
                    }
                }
                for (AbstractCard card1 : AbstractDungeon.player.hand.group) {
                    if (card1 instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) card1).onBurialForCard();
                    }
                    if (modLoaded) {
                        if (burial.isInstance(card1)) {
                            Method method = burial.getDeclaredMethod("onBurialForCard");
                            method.invoke(card1);
                        }
                    }
                }
                for (AbstractCard card1 : AbstractDungeon.player.discardPile.group) {
                    if (card1 instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) card1).onBurialForCard();
                    }
                    if (modLoaded) {
                        if (burial.isInstance(card1)) {
                            Method method = burial.getDeclaredMethod("onBurialForCard");
                            method.invoke(card1);
                        }
                    }
                }
                for (AbstractCard card1 : AbstractDungeon.player.exhaustPile.group) {
                    if (card1 instanceof PostBurialSubscriber) {
                        ((PostBurialSubscriber) card1).onBurialForCard();
                    }
                    if (modLoaded) {
                        if (burial.isInstance(card1)) {
                            Method method = burial.getDeclaredMethod("onBurialForCard");
                            method.invoke(card1);
                        }
                    }
                }
            }
            tickDuration();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

package demoMod.scapegoat.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.cards.tempCards.AbnormalityTemplateCard;
import demoMod.scapegoat.powers.ProtectionPower;

import java.util.ArrayList;

public class PageOfFreischutz extends AbstractAbnormalityPages implements CustomSavable<int[]> {
    public static final String ID = Scapegoat.makeID("PageOfFreischutz");
    private int strength;
    private boolean killed;

    public PageOfFreischutz() {
        super(ID, RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
        this.cardsToPreview.add(new AbnormalityTemplateCard("Request", this.DESCRIPTIONS[1], this.DESCRIPTIONS[10], this));
        this.cardsToPreview.add(new AbnormalityTemplateCard("TheSeventhBullet", this.DESCRIPTIONS[4], this.DESCRIPTIONS[11], this));
        this.cardsToPreview.add(new AbnormalityTemplateCard("DarkFlame", this.DESCRIPTIONS[7], this.DESCRIPTIONS[12], this));
        ((AbnormalityTemplateCard)this.cardsToPreview.get(1)).followupAction = () -> {
            this.counter = 0;
        };
    }

    @Override
    public void onEquip() {
        AbstractDungeon.closeCurrentScreen();
        if ((AbstractDungeon.getCurrRoom()).rewardTime) {
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        ArrayList<AbstractCard> copyList = new ArrayList<>();
        for (AbstractCard card : this.cardsToPreview) {
            copyList.add(card.makeSameInstanceOf());
        }
        Scapegoat.addToBot(new ChooseOneAction(copyList));
    }

    @Override
    public void atBattleStart() {
        if (this.chooseCard[0] == 0) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    if (AbstractDungeon.player.hand.size() > 3) {
                        ArrayList<AbstractCard> list = new ArrayList<>(AbstractDungeon.player.hand.group);
                        while (list.size() > 3) {
                            list.remove(AbstractDungeon.cardRng.random(list.size() - 1));
                        }
                        for (AbstractCard card : list) {
                            addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                        }
                    } else {
                        for (AbstractCard card : AbstractDungeon.player.hand.group) {
                            addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                        }
                    }
                    addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, 18, DamageInfo.DamageType.NORMAL, AttackEffect.BLUNT_LIGHT));
                    for (AbstractMonster monster : (AbstractDungeon.getMonsters()).monsters) {
                        if (!monster.isDeadOrEscaped()) {
                            addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new VulnerablePower(monster, 2, false), 2));
                        }
                    }
                    isDone = true;
                }
            });
        } else if (this.chooseCard[0] == 1) {
            this.strength = 0;
        }
    }

    @Override
    public void atTurnStart() {
        if (this.chooseCard[0] == 2) {
            this.killed = false;
            beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (this.chooseCard[0] == 1) {
            if (c.type != AbstractCard.CardType.ATTACK) {
                return;
            }
            if (this.strength < 7) {
                this.strength++;
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
            }
            this.counter++;
            if (this.counter >= 7) {
                this.counter = 0;
                addToBot(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
                addToBot(new ExhaustSpecificCardAction(c, AbstractDungeon.player.discardPile));
                if (AbstractDungeon.player.discardPile.size() > 0) {
                    addToBot(new ExhaustSpecificCardAction(AbstractDungeon.player.discardPile.getRandomCard(true), AbstractDungeon.player.discardPile));
                }
            }
        }
    }

    @Override
    public void onExhaust(AbstractCard card) {
        super.onExhaust(card);
        if (this.chooseCard[0] == 2) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ProtectionPower(AbstractDungeon.player, 1), 1));
        }
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        if (this.chooseCard[0] == 2 &&
                !this.killed) {
            addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 2));
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        if (this.chooseCard[0] == 2) {
            this.killed = true;
            this.pulse = false;
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
        if (this.chooseCard[0] == 2) {
            this.killed = true;
            this.pulse = false;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public int[] onSave() {
        return this.chooseCard;
    }

    @Override
    public void onLoad(int[] arg0) {
        if (arg0 == null) {
            return;
        }
        if (arg0[0] >= 0) {
            this.chooseCard[0] = arg0[0];
        }
        if (this.chooseCard[0] >= 0) {
            this.cardsToPreview.get(this.chooseCard[0]).onChoseThisOption();
        }
    }
}

package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.interfaces.CardAddToHandSubscriber;
import demoMod.scapegoat.interfaces.PostBurialSubscriber;
import demoMod.scapegoat.patches.GameActionManagerPatch;

public class DelayedFlame extends CustomCard implements PostBurialSubscriber, CardAddToHandSubscriber {
    public static final String ID = Scapegoat.makeID("DelayedFlame");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/delayedFlame.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;

    public DelayedFlame() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseDamage = 5;
        this.baseMagicNumber = this.magicNumber = 3;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }

    @Override
    public void triggerWhenDrawn() {
        this.setCostForTurn(this.cost - GameActionManagerPatch.AddFieldPatch.totalBurialThisTurn.get(AbstractDungeon.actionManager));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0;i<this.magicNumber;i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void atTurnStart() {
        this.resetAttributes();
        this.applyPowers();
    }

    @Override
    public void onBurial() {
    }

    @Override
    public void onBurialForCard() {
        this.setCostForTurn(this.costForTurn - 1);
    }

    @Override
    public void onCardAddToHand() {
        triggerWhenDrawn();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.interfaces.AbstractSecondaryMCard;
import demoMod.scapegoat.powers.NextTurnDamagePower;

public class ReturnedBlade extends CustomCard implements AbstractSecondaryMCard {
    public static final String ID = Scapegoat.makeID("ReturnedBlade");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/returnedBlade.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;

    private int secondaryM = 5;

    public ReturnedBlade() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseDamage = 8;
        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
            this.secondaryM = 5;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(p, p, new NextTurnDamagePower(getValue())));
    }

    @Override
    public boolean isSecondaryMModified() {
        return getValue() != getBaseValue();
    }

    @Override
    public int getValue() {
        if (AbstractDungeon.player == null || AbstractDungeon.getCurrMapNode() == null || AbstractDungeon.getMonsters() == null || AbstractDungeon.getMonsters().monsters == null || AbstractDungeon.getMonsters().monsters.size() == 0) return getBaseValue();
        int t = this.baseDamage;
        this.baseDamage = getBaseValue();
        this.calculateCardDamage(null);
        this.baseDamage = t;
        int u = this.damage;
        this.calculateCardDamage(null);
        return u;
    }

    @Override
    public int getBaseValue() {
        return this.baseDamage + this.secondaryM;
    }

    @Override
    public boolean isSecondaryMUpgraded() {
        return this.upgraded;
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

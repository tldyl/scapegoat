package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.interfaces.AbstractSecondaryMCard;
import demoMod.scapegoat.powers.CrossDominatePower;

public class CrossDominate extends CustomCard implements AbstractSecondaryMCard {
    public static final String ID = Scapegoat.makeID("CrossDominate");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/crossDominate.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private int secondaryM;

    public CrossDominate() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 4;
        this.secondaryM = 3;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-1);
            this.secondaryM = 2;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(p, new DamageInfo(p, this.magicNumber, DamageInfo.DamageType.HP_LOSS)));
        addToBot(new ApplyPowerAction(p, p, new CrossDominatePower(this.secondaryM)));
    }

    @Override
    public boolean isSecondaryMModified() {
        return false;
    }

    @Override
    public int getValue() {
        return this.secondaryM;
    }

    @Override
    public int getBaseValue() {
        return this.secondaryM;
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

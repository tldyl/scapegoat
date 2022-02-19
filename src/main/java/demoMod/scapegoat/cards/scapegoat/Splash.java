package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.interfaces.AbstractSecondaryMCard;
import demoMod.scapegoat.powers.BulletPower;
import demoMod.scapegoat.powers.SplashPower;

public class Splash extends CustomCard implements AbstractSecondaryMCard {
    public static final String ID = Scapegoat.makeID("Splash");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/splash.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;

    private int secondaryM;

    public Splash() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 4;
        this.secondaryM = 4;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
            this.secondaryM = 6;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new BulletPower(m, this.magicNumber)));
        addToBot(new ApplyPowerAction(m, p, new SplashPower(m, this.secondaryM)));
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

package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.powers.ForTheSinPower;

public class ForTheSin extends CustomCard {
    public static final String ID = Scapegoat.makeID("ForTheSin");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/forTheSin.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public ForTheSin() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ForTheSinPower(this.magicNumber)));
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

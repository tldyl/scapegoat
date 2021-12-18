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
import demoMod.scapegoat.powers.TemporaryCollaborationPower;

public class TemporaryCollaboration extends CustomCard implements AbstractSecondaryMCard {
    public static final String ID = Scapegoat.makeID("TemporaryCollaboration");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/temporaryCollaboration.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public TemporaryCollaboration() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TemporaryCollaborationPower(this.magicNumber, getValue())));
    }

    @Override
    public boolean isSecondaryMModified() {
        return false;
    }

    @Override
    public int getValue() {
        return 1;
    }

    @Override
    public int getBaseValue() {
        return 1;
    }

    @Override
    public boolean isSecondaryMUpgraded() {
        return false;
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

package demoMod.scapegoat.cards.purple;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;

public class Alpha2 extends CustomCard {
    public static final String ID = Scapegoat.makeID("Alpha2");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/alpha2.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public Alpha2() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.PURPLE, RARITY, TARGET);
        this.exhaust = true;
        this.cardsToPreview = new Beta2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.isInnate = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInDrawPileAction(new Beta2(), 1, true, true));
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

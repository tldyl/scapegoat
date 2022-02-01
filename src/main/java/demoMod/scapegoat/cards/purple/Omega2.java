package demoMod.scapegoat.cards.purple;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.powers.Omega2Power;

public class Omega2 extends CustomCard {
    public static final String ID = Scapegoat.makeID("Omega2");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/omega2.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL;

    private static final int COST = 3;

    public Omega2() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 50;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(10);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature target;
        if (AbstractDungeon.cardRandomRng.randomBoolean()) {
            target = AbstractDungeon.getRandomMonster();
        } else {
            target = p;
        }
        if (target != null) {
            addToBot(new ApplyPowerAction(target, p, new Omega2Power(target, this.magicNumber)));
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

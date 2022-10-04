package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.actions.RecallAction;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.interfaces.PostBurialSubscriber;

import java.util.ArrayList;

public class Shard extends CustomCard implements PostBurialSubscriber {
    public static final String ID = Scapegoat.makeID("Shard");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/shard.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;

    public Shard() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
        this.tags = new ArrayList<>();
        this.tags.add(AbstractCardEnum.FLASH);
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
        addToBot(new RecallAction(this.magicNumber));
    }

    @Override
    public void onBurial() {
        use(AbstractDungeon.player, null);
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.discardPile, true));
    }

    @Override
    public void triggerOnExhaust() {
        use(AbstractDungeon.player, null);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

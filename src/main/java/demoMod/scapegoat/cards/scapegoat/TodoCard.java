package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;

public class TodoCard extends CustomCard {
    public static final String ID = Scapegoat.makeID("TodoCard");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/tailWhip.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    private int index;

    public TodoCard(int index) {
        super(ID + index, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.index = index;
        if (this.index % 7 == 0) this.type = CardType.POWER;
        if (this.index % 3 == 0) this.type = CardType.ATTACK;
        if (this.index % 7 == 0) this.rarity = CardRarity.UNCOMMON;
        if (this.index % 9 == 0) this.rarity = CardRarity.RARE;
        switch (this.type) {
            case ATTACK:
                this.loadCardImage(Scapegoat.getResourcePath("cards/todo_a.png"));
                break;
            case SKILL:
                this.loadCardImage(Scapegoat.getResourcePath("cards/todo_s.png"));
                break;
            case POWER:
                this.loadCardImage(Scapegoat.getResourcePath("cards/todo_p.png"));
                break;
        }
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public AbstractCard makeCopy() {
        return new TodoCard(this.index);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

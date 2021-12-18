package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;

public class Awe extends CustomCard {
    public static final String ID = Scapegoat.makeID("Awe");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/awe.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public Awe() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.target = CardTarget.ALL_ENEMY;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!this.upgraded) {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false)));
            if (m.intent.name().contains("ATTACK")) {
                addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false)));
            }
        } else {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, this.magicNumber, false)));
                if (monster.intent.name().contains("ATTACK")) {
                    addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, this.magicNumber, false)));
                }
            }
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

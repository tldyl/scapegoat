package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;

public class BloodyThorns extends CustomCard {
    public static final String ID = Scapegoat.makeID("BloodyThorns");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/bloodyThorns.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;

    public BloodyThorns() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseDamage = this.damage = 18;
        this.misc = 3;
        this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeDamage(3);
            this.misc++;
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Scapegoat.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        Scapegoat.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if ((m.isDying || m.isDead) && !m.hasPower(MinionPower.POWER_ID)) {
                    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                        if (card.uuid.equals(BloodyThorns.this.uuid)) {
                            card.misc += baseMagicNumber;
                            card.applyPowers();
                            break;
                        }
                    }
                }
                isDone = true;
            }
        });
        Scapegoat.addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, this.misc)));
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

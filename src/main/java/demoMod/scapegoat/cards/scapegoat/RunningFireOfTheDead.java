package demoMod.scapegoat.cards.scapegoat;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;

public class RunningFireOfTheDead extends CustomCard {
    public static final String ID = Scapegoat.makeID("RunningFireOfTheDead");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/runningFireOfTheDead.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public RunningFireOfTheDead() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseDamage = 4;
        this.baseMagicNumber = this.magicNumber = 2;
        this.cardsToPreview = new SerialBullet();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
            this.cardsToPreview.upgrade();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            AbstractGameEffect e = new ThrowDaggerEffect(m.hb.cX, m.hb.cY);
            ReflectionHacks.setPrivate(e, ThrowDaggerEffect.class, "playedSound", true);
            this.addToBot(new SFXAction("GUN_FIRE_POLARIS"));
            this.addToBot(new VFXAction(e));
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
            AbstractCard card = new SerialBullet();
            if (this.upgraded) {
                card.upgrade();
            }
            addToBot(new MakeTempCardInHandAction(card, this.magicNumber));
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

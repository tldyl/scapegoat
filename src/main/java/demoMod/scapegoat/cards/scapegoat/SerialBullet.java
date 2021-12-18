package demoMod.scapegoat.cards.scapegoat;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.powers.BulletPower;

public class SerialBullet extends CustomCard {
    public static final String ID = Scapegoat.makeID("SerialBullet");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/serialBullet.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 0;

    public SerialBullet() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.baseDamage = 2;
        this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
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
        if (m != null) {
            for (int i=0;i<2;i++) {
                AbstractGameEffect e = new ThrowDaggerEffect(m.hb.cX, m.hb.cY);
                ReflectionHacks.setPrivate(e, ThrowDaggerEffect.class, "playedSound", true);
                this.addToBot(new SFXAction("GUN_FIRE_POLARIS"));
                this.addToBot(new VFXAction(e));
                addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
            }
            if (!m.hasPower(BulletPower.POWER_ID)) {
                addToBot(new ApplyPowerAction(m, p, new BulletPower(m, this.magicNumber)));
            }
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

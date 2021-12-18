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
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.powers.BulletPower;

public class BlazeAway extends CustomCard {
    public static final String ID = Scapegoat.makeID("BlazeAway");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/blazeAway.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;

    public BlazeAway() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
        this.baseDamage = 3;
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
        calculateCardDamage(null);
        final int finalDamage = this.damage;
        for (int i=0;i<this.magicNumber;i++) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractMonster monster = AbstractDungeon.getRandomMonster();
                    if (monster != null) {
                        addToTop(new ApplyPowerAction(monster, p, new BulletPower(monster, 1)));
                        addToTop(new DamageAction(monster, new DamageInfo(p, finalDamage, BlazeAway.this.damageTypeForTurn)));
                        AbstractGameEffect e = new ThrowDaggerEffect(monster.hb.cX, monster.hb.cY);
                        ReflectionHacks.setPrivate(e, ThrowDaggerEffect.class, "playedSound", true);
                        addToTop(new VFXAction(e));
                        addToTop(new SFXAction("GUN_FIRE_POLARIS"));
                    }
                    isDone = true;
                }
            });
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

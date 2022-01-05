package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.GrandFinalEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.interfaces.PostBurialSubscriber;
import demoMod.scapegoat.patches.AbstractCardPatch;

public class CurtainCall extends CustomCard implements PostBurialSubscriber {
    public static final String ID = Scapegoat.makeID("CurtainCall");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/curtainCall.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = -2;

    public CurtainCall() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.baseDamage = 20;
        this.baseMagicNumber = this.magicNumber = 30;
        AbstractCardPatch.AddFieldPatch.isBottom.set(this, true);
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
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void onBurial() {
        AbstractPlayer p = AbstractDungeon.player;
        calculateCardDamage(null);
        if (this.damage > 0) {
            if (Settings.FAST_MODE) {
                this.addToBot(new VFXAction(new GrandFinalEffect(), 0.7F));
            } else {
                this.addToBot(new VFXAction(new GrandFinalEffect(), 1.0F));
            }
            this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                CurtainCall.this.baseDamage += CurtainCall.this.magicNumber;
                isDone = true;
            }
        });
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        this.baseDamage = 0;
    }

    @Override
    public void triggerOnExhaust() {
        onBurial();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.interfaces.OnPlayerDeathCard;

public class SelfSacrifice extends CustomCard implements OnPlayerDeathCard {
    public static final String ID = Scapegoat.makeID("SelfSacrifice");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/selfSacrifice.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private boolean shouldResurrect = false;

    public SelfSacrifice() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
        this.exhaust = true;
        this.selfRetain = true;
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
        shouldResurrect = true;
        p.damage(new DamageInfo(p, 10, DamageInfo.DamageType.HP_LOSS));
        shouldResurrect = false;
        p.isDead = false;
        if (p.currentHealth <= 0) {
            p.heal(p.maxHealth);
            p.masterDeck.removeCard(ID);
            this.purgeOnUse = true;
        }
        p.increaseMaxHp(this.magicNumber, true);
        addToTop(new VFXAction(new FlashAtkImgEffect(p.hb.cX, p.hb.cY, AbstractGameAction.AttackEffect.SLASH_VERTICAL)));
    }

    @Override
    public boolean onPlayerDeath(AbstractPlayer p, DamageInfo info) {
        return !shouldResurrect;
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}

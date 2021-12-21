package demoMod.scapegoat.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.cards.scapegoat.SerialBullet;
import demoMod.scapegoat.utils.PowerRegionLoader;

public class MasteryOfFirearmsPower extends AbstractPower {
    public static final String POWER_ID = Scapegoat.makeID("MasteryOfFirearmsPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private boolean upgraded;

    public MasteryOfFirearmsPower(int amount, boolean upgraded) {
        if (this.upgraded) {
            this.name = NAME + "+";
        } else {
            this.name = NAME;
        }
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.upgraded = upgraded;
        PowerRegionLoader.loadRegion(this);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.upgraded) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (this.upgraded) return;
        if (power instanceof MasteryOfFirearmsPower && target == this.owner) {
            MasteryOfFirearmsPower masteryOfFirearmsPower = (MasteryOfFirearmsPower) power;
            if (masteryOfFirearmsPower.upgraded) {
                this.upgraded = true;
                this.name = NAME + "+";
                this.updateDescription();
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            AbstractCard card = new SerialBullet();
            if (this.upgraded) card.upgrade();
            this.addToBot(new MakeTempCardInHandAction(card, this.amount, false));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

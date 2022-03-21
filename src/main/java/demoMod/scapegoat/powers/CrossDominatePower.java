package demoMod.scapegoat.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.interfaces.PostBurialSubscriber;
import demoMod.scapegoat.utils.PowerRegionLoader;

public class CrossDominatePower extends TwoAmountPower implements PostBurialSubscriber {
    public static final String POWER_ID = Scapegoat.makeID("CrossDominatePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private int defaultAmount;

    public CrossDominatePower(int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = 1;
        this.amount2 = this.defaultAmount = amount;
        PowerRegionLoader.loadRegion(this);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount2 + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += 1;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof CrossDominatePower) {
            TwoAmountPower power1 = (TwoAmountPower) power;
            this.amount2 = Math.min(this.amount2, power1.amount2);
        }
    }

    @Override
    public void onBurial() {
    }

    @Override
    public void onBurial(AbstractCard card) {
        this.amount2--;
        if (amount2 == 0) {
            this.flash();
            this.amount2 = this.defaultAmount;
            addToBot(new DrawCardAction(this.amount));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

package demoMod.scapegoat.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import demoMod.scapegoat.Scapegoat;

public class PriorRepairPower extends TwoAmountPower {
    public static final String POWER_ID = Scapegoat.makeID("PriorRepairPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private int amount3;

    public PriorRepairPower(int drawAmount, int energyAmount, int turns) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = turns;
        this.amount2 = drawAmount;
        this.amount3 = energyAmount;
        this.updateDescription();
        this.loadRegion("draw");
        this.isTurnBased = true;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount3 + DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.flash();
        addToBot(new GainEnergyAction(this.amount3));
        addToBot(new DrawCardAction(this.owner, this.amount2));
        addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

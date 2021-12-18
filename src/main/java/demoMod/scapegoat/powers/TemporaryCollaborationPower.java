package demoMod.scapegoat.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.actions.BurialAction;
import demoMod.scapegoat.actions.RecallAction;

public class TemporaryCollaborationPower extends TwoAmountPower {
    public static final String POWER_ID = Scapegoat.makeID("TemporaryCollaborationPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public TemporaryCollaborationPower(int amount, int amount2) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.amount2 = amount2;
        this.loadRegion("tools");
        this.updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (target == this.owner && power.ID.equals(POWER_ID)) {
            this.amount2 += ((TwoAmountPower) power).amount2;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.flash();
        addToBot(new BurialAction(this.amount));
        addToBot(new RecallAction(this.amount2));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

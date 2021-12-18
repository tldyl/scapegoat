package demoMod.scapegoat.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.actions.BurialAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WashAwayPower extends TwoAmountPower {
    public static final String POWER_ID = Scapegoat.makeID("WashAwayPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public WashAwayPower(int removeDebuffAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = removeDebuffAmount;
        this.amount2 = 6;
        this.updateDescription();
        this.loadRegion("wireheading");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        List<AbstractPower> powers = new ArrayList<>();
        for (AbstractPower power : this.owner.powers) {
            if (power.type == PowerType.DEBUFF) {
                powers.add(power);
            }
        }
        if (powers.size() > 0) {
            this.flash();
            addToBot(new BurialAction(this.amount2));
            Collections.shuffle(powers, new Random(AbstractDungeon.cardRandomRng.random(Long.MAX_VALUE)));
            for (int i=0;i<this.amount;i++) {
                if (powers.isEmpty()) break;
                addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, powers.remove(0)));
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

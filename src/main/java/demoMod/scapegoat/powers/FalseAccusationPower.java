package demoMod.scapegoat.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.interfaces.PostBurialSubscriber;
import demoMod.scapegoat.utils.PowerRegionLoader;

public class FalseAccusationPower extends AbstractPower implements PostBurialSubscriber {
    public static final String POWER_ID = Scapegoat.makeID("FalseAccusationPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private boolean upgraded;

    public FalseAccusationPower(int amount, boolean upgraded) {
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
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (this.upgraded) return;
        if (power instanceof FalseAccusationPower && target == this.owner) {
            FalseAccusationPower falseAccusationPower = (FalseAccusationPower) power;
            if (falseAccusationPower.upgraded) {
                this.upgraded = true;
                this.name = NAME + "+";
                this.updateDescription();
            }
        }
    }

    @Override
    public void onBurial() {
        this.flash();
        addToBot(new ApplyPowerAction(this.owner, this.owner, new PayBackPower(this.owner, this.amount)));
        if (this.upgraded) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(monster, this.owner, new BulletPower(monster, this.amount)));
                }
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

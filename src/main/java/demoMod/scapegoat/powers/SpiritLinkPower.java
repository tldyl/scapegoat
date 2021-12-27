package demoMod.scapegoat.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.Scapegoat;

import java.util.ArrayList;
import java.util.List;

public class SpiritLinkPower extends AbstractPower {
    public static final String POWER_ID = Scapegoat.makeID("SpiritLinkPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private boolean isSoul;
    private List<AbstractMonster> target = new ArrayList<>();

    public SpiritLinkPower(AbstractCreature owner, AbstractMonster target, boolean isSoul) {
        this.name = NAME;
        this.ID = POWER_ID;
        if (isSoul) {
            this.ID += target.id;
        } else {
            this.ID += owner.id;
        }
        this.owner = owner;
        this.amount = -1;
        this.updateDescription();
        this.loadRegion("cExplosion");
        this.isSoul = isSoul;
        this.target.add(target);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && isSoul) {
            this.flash();
            if (this.target.get(0).hasPower(POWER_ID)) {
                this.target.get(0).getPower(POWER_ID).flash();
            }
            this.target.get(0).damage(new DamageInfo(this.owner, damageAmount * 2, DamageInfo.DamageType.HP_LOSS));
        }
        return damageAmount;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof SpiritLinkPower && target == this.owner) {
            SpiritLinkPower power1 = (SpiritLinkPower) power;
            this.target.addAll(power1.target);
        }
    }

    @Override
    public void onDeath() {
        if (!this.isSoul) {
            for (AbstractMonster monster : this.target) {
                addToBot(new SuicideAction(monster, true));
            }
        } else {
            addToBot(new RemoveSpecificPowerAction(this.target.get(0), this.owner, this.ID));
        }
    }

    @Override
    public void playApplyPowerSfx() {
        int roll = MathUtils.random(0, 2);
        if (roll == 0) {
            CardCrawlGame.sound.play("DEBUFF_1");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("DEBUFF_2");
        } else {
            CardCrawlGame.sound.play("DEBUFF_3");
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

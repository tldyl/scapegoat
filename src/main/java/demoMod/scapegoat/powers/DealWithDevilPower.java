package demoMod.scapegoat.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.GainPowerEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.effects.FlashPowerOnCreatureEffect;
import demoMod.scapegoat.utils.PowerRegionLoader;

import java.util.ArrayList;

public class DealWithDevilPower extends AbstractPower {
    public static final String POWER_ID = Scapegoat.makeID("DealWithDevilPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public DealWithDevilPower(int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = amount;
        PowerRegionLoader.loadRegion(this);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.flash();
            addToBot(new DamageAllEnemiesAction(this.owner, DamageInfo.createDamageMatrix(this.amount, true), DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void flash() {
        ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");
        effect.add(new GainPowerEffect(this));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped()) {
                AbstractDungeon.effectList.add(new FlashPowerOnCreatureEffect(this, monster));
            }
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

package demoMod.scapegoat.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.OmegaFlashEffect;
import demoMod.scapegoat.Scapegoat;

public class Omega2Power extends AbstractPower {
    public static final String POWER_ID = Scapegoat.makeID("Omega2Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public Omega2Power(AbstractCreature owner, int amount) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("omega");
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractPlayer p = AbstractDungeon.player;
        this.flash();
        if (isPlayer && this.owner instanceof AbstractPlayer) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != null && !m.isDeadOrEscaped()) {
                    if (Settings.FAST_MODE) {
                        addToBot(new VFXAction(new OmegaFlashEffect(m.hb.cX, m.hb.cY)));
                    } else {
                        addToBot(new VFXAction(new OmegaFlashEffect(m.hb.cX, m.hb.cY), 0.2F));
                    }
                }
            }
            addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(this.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
        }
    }

    @Override
    public void duringTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.owner instanceof AbstractMonster) {
            addToBot(new VFXAction(new OmegaFlashEffect(p.hb.cX, p.hb.cY)));
            addToBot(new DamageAction(p, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }
}

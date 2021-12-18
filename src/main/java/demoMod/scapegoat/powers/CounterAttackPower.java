package demoMod.scapegoat.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.Scapegoat;

public class CounterAttackPower extends AbstractPower {
    public static final String POWER_ID = Scapegoat.makeID("CounterAttackPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private boolean isOwnerTurn = true;

    public CounterAttackPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.loadRegion("malleable");
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        this.isOwnerTurn = !this.isOwnerTurn;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.isOwnerTurn = this.owner instanceof AbstractPlayer != isPlayer;
        System.out.println(this.isOwnerTurn);
    }

    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        if (!this.isOwnerTurn && info.owner == this.owner) {
            this.flash();
            return damageAmount * 2;
        }
        return damageAmount;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

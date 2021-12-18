package demoMod.scapegoat.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.interfaces.PreDamageSubscriber;
import demoMod.scapegoat.patches.GameActionManagerPatch;
import demoMod.scapegoat.relics.MarkOfThePayback;
import demoMod.scapegoat.utils.PowerRegionLoader;

public class PayBackPower extends AbstractPower implements PreDamageSubscriber {
    public static final String POWER_ID = Scapegoat.makeID("PayBackPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private float multi = 1.0F;

    public PayBackPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        PowerRegionLoader.loadRegion(this);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onInitialApplication() {
        GameActionManagerPatch.AddFieldPatch.paybackGained.set(AbstractDungeon.actionManager, GameActionManagerPatch.AddFieldPatch.paybackGained.get(AbstractDungeon.actionManager) + this.amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        GameActionManagerPatch.AddFieldPatch.paybackGained.set(AbstractDungeon.actionManager, GameActionManagerPatch.AddFieldPatch.paybackGained.get(AbstractDungeon.actionManager) + stackAmount);
    }

    @Override
    public void justAttacked(DamageInfo info) {
        if (AbstractDungeon.player.hasRelic(MarkOfThePayback.ID)) {
            this.multi = 1.25F;
        }
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner && this.amount > 0) {
            this.flash();
            this.addToTop(new DamageAction(info.owner, new DamageInfo(this.owner, (int) (info.output * multi), DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
            this.amount--;
            if (this.amount <= 0) {
                addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

package demoMod.scapegoat.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import demoMod.scapegoat.Scapegoat;

public class MyAttackDamageRandomEnemyAction extends AbstractGameAction {
    private AttackEffect effect;
    private int damageAmount;

    public MyAttackDamageRandomEnemyAction(AttackEffect effect, int damageAmount) {
        this.effect = effect;
        this.damageAmount = damageAmount;
    }

    @Override
    public void update() {
        if (isDone) return;
        this.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        while (!(this.target instanceof SpireShield) && !(this.target instanceof SpireSpear) && this.target != null) {
            this.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        }
        if (this.target != null) {
            if (AttackEffect.LIGHTNING == this.effect) {
                Scapegoat.addToTop(new DamageAction(this.target, new DamageInfo(AbstractDungeon.player, damageAmount, DamageInfo.DamageType.THORNS), AttackEffect.NONE));
                Scapegoat.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                Scapegoat.addToTop(new VFXAction(new LightningEffect(this.target.hb.cX, this.target.hb.cY)));
            } else {
                Scapegoat.addToTop(new DamageAction(this.target, new DamageInfo(AbstractDungeon.player, damageAmount, DamageInfo.DamageType.THORNS), this.effect));
            }
        }
        isDone = true;
    }
}

package demoMod.scapegoat.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.monsters.BaredSoul;
import demoMod.scapegoat.powers.SpiritLinkPower;

public class BaresTheSoulAction extends AbstractGameAction {
    private AbstractPlayer p;

    public BaresTheSoulAction(AbstractMonster m, int maxHealth) {
        this.target = m;
        this.duration = AbstractGameAction.DEFAULT_DURATION;
        this.p = AbstractDungeon.player;
        this.amount = maxHealth;
    }

    @Override
    public void update() {
        AbstractMonster soul = new BaredSoul(0, 0, this.amount);
        addToTop(new ApplyPowerAction(target, p, new SpiritLinkPower(target, soul, false)));
        soul.drawX = target.drawX - 220.0F * Settings.scale;
        soul.drawY = target.drawY + 280.0F * Settings.scale;
        addToTop(new ApplyPowerAction(soul, p, new SpiritLinkPower(soul, (AbstractMonster) target, true)));
        addToTop(new SpawnMonsterAction(soul, true, -99));
        this.isDone = true;
    }
}

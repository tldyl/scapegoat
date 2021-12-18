package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.effects.BlueFireAttackEffect;

public class AzureFlame extends CustomRelic implements OnPlayerDeathRelic {
    public static final String ID = Scapegoat.makeID("AzureFlame");
    public static final String IMG_PATH = "relics/azureFlame.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));

    public AzureFlame() {
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.pulse) {
            this.flash();
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDeadOrEscaped()) {
                    addToTop(new RelicAboveCreatureAction(monster, this));
                    addToBot(new VFXAction(new BlueFireAttackEffect(monster.hb.cX, monster.hb.cY, true)));
                }
            }
            addToBot(new SFXAction("ATTACK_FIRE"));
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(66, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void onVictory() {
        this.stopPulse();
        this.setCounter(this.counter);
    }

    public void setCounter(int setCounter) {
        if (setCounter == -2) {
            this.usedUp();
            this.counter = -2;
        }
    }

    @Override
    public boolean onPlayerDeath(AbstractPlayer p, DamageInfo info) {
        if (this.counter == -1) {
            this.flash();
            addToTop(new RelicAboveCreatureAction(p, this));
            int healAmt = (int) Math.ceil(p.maxHealth * 0.3);
            if (healAmt < 1) {
                healAmt = 1;
            }
            p.heal(healAmt, true);
            this.beginLongPulse();
            this.counter = -2;
            return false;
        }
        return true;
    }
}

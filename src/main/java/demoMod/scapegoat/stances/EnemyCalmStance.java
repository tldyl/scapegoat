package demoMod.scapegoat.stances;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.stance.CalmParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;

public class EnemyCalmStance extends CalmStance {
    private AbstractMonster owner;

    public EnemyCalmStance(AbstractMonster owner) {
        this.owner = owner;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.img != null) {
            sb.setColor(this.c);
            sb.setBlendFunction(770, 1);
            sb.draw(this.img, this.owner.drawX - 256.0F + this.owner.animX, this.owner.drawY - 256.0F + this.owner.animY + this.owner.hb_h / 2.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, -this.angle, 0, 0, 512, 512, false, false);
            sb.setBlendFunction(770, 771);
        }
    }

    @Override
    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.04F;
                AbstractGameEffect effect = new CalmParticleEffect();
                ReflectionHacks.setPrivate(effect, CalmParticleEffect.class, "x", this.owner.hb.cX + MathUtils.random(100.0F, 160.0F) * Settings.scale - 32.0F);
                ReflectionHacks.setPrivate(effect, CalmParticleEffect.class, "y", this.owner.hb.cY + MathUtils.random(-50.0F, 220.0F) * Settings.scale - 32.0F);
                AbstractDungeon.effectsQueue.add(effect);
            }
        }

        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            AbstractGameEffect effect = new StanceAuraEffect("Calm");
            ReflectionHacks.setPrivate(effect, StanceAuraEffect.class, "x", this.owner.hb.cX + MathUtils.random(-this.owner.hb.width / 16.0F, this.owner.hb.width / 16.0F));
            ReflectionHacks.setPrivate(effect, StanceAuraEffect.class, "y", this.owner.hb.cY + MathUtils.random(-this.owner.hb.height / 16.0F, this.owner.hb.height / 12.0F));
            AbstractDungeon.effectsQueue.add(effect);
        }
    }

    public void onExitStance() {
        this.stopIdleSfx();
    }
}

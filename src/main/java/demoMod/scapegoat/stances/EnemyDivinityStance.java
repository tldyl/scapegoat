package demoMod.scapegoat.stances;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityStanceChangeParticle;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;

public class EnemyDivinityStance extends DivinityStance {
    private AbstractMonster owner;
    private static long sfxId = -1L;

    public EnemyDivinityStance(AbstractMonster owner) {
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
                this.particleTimer = 0.2F;
                AbstractGameEffect effect = new DivinityParticleEffect();
                ReflectionHacks.setPrivate(effect, DivinityParticleEffect.class, "x", this.owner.hb.cX + MathUtils.random(-this.owner.hb.width / 2.0F - 50.0F * Settings.scale, this.owner.hb.width / 2.0F + 50.0F * Settings.scale));
                ReflectionHacks.setPrivate(effect, DivinityParticleEffect.class, "y", this.owner.hb.cY + MathUtils.random(-this.owner.hb.height / 2.0F + 10.0F * Settings.scale, this.owner.hb.height / 2.0F - 20.0F * Settings.scale));
                AbstractDungeon.effectsQueue.add(effect);
            }
        }

        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);

            AbstractGameEffect effect = new StanceAuraEffect("Divinity");
            ReflectionHacks.setPrivate(effect, StanceAuraEffect.class, "x", this.owner.hb.cX + MathUtils.random(-this.owner.hb.width / 16.0F, this.owner.hb.width / 16.0F));
            ReflectionHacks.setPrivate(effect, StanceAuraEffect.class, "y", this.owner.hb.cY + MathUtils.random(-this.owner.hb.height / 16.0F, this.owner.hb.height / 12.0F));
            AbstractDungeon.effectsQueue.add(effect);
        }
    }

    @Override
    public void onEnterStance() {
        if (sfxId != -1L) {
            this.stopIdleSfx();
        }

        CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_DIVINITY");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.PINK, true));
        for(int i = 0; i < 20; ++i) {
            AbstractDungeon.effectsQueue.add(new DivinityStanceChangeParticle(Color.PINK, this.owner.hb.cX, this.owner.hb.cY));
        }
    }

    @Override
    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_DIVINITY", sfxId);
            sfxId = -1L;
        }
    }
}

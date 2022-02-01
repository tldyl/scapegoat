package demoMod.scapegoat.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightRayFlyOutEffect;

public class WatcherEliteSanctityEffect extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion img;
    private AbstractMonster owner;
    private float vfxTimer;

    public WatcherEliteSanctityEffect(AbstractMonster owner) {
        this.img = ImageMaster.BORDER_GLOW_2;
        this.owner = owner;
        this.color = Color.GOLD.cpy();
        this.color.a = 0.0F;
    }

    @Override
    public void update() {
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F) {
            this.vfxTimer = MathUtils.random(0.02F, 0.08F);
            for(int i = 0; i < 3; ++i) {
                AbstractDungeon.effectsQueue.add(new LightRayFlyOutEffect(owner.hb.cX, owner.hb.cY, new Color(1.0F, 0.9F, 0.7F, 0.0F)));
            }
        }

        if (1.0F - this.duration < 0.1F) {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, (1.0F - this.duration) * 10.0F);
        } else {
            this.color.a = Interpolation.pow2Out.apply(0.0F, 1.0F, this.duration);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
    }

    public void stop() {
        this.isDone = true;
    }
}

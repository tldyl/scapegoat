package demoMod.scapegoat.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.scapegoat.Scapegoat;

public class BlueFireAttackEffect extends AbstractGameEffect {
    private float x;
    private float y;
    public TextureAtlas.AtlasRegion img;

    public BlueFireAttackEffect(float x, float y, boolean mute) {
        this.img = new TextureAtlas.AtlasRegion(new Texture(Scapegoat.getResourcePath("effects/blueAttackFire.png")), 0, 0, 196, 196);
        this.x = x - (float)this.img.packedWidth / 2.0F;
        y -= (float)this.img.packedHeight / 2.0F;
        this.y = y;
        this.color = Color.WHITE.cpy();
        this.scale = Settings.scale;
        this.duration = 0.6F;
        this.startingDuration = 0.6F;
        if (!mute) {
            CardCrawlGame.sound.play("ATTACK_FIRE");
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.img != null) {
            sb.setColor(this.color);
            sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
        }
    }

    @Override
    public void dispose() {
        this.img.getTexture().dispose();
    }
}

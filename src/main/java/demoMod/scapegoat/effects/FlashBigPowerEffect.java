package demoMod.scapegoat.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FlashBigPowerEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private Texture img;
    private static final int W = 32;
    private float scale = Settings.scale;

    public FlashBigPowerEffect(AbstractPower power) {
        if (!power.owner.isDeadOrEscaped()) {
            this.x = power.owner.hb.cX;
            this.y = power.owner.hb.cY;
        }
        this.img = power.img;
        this.duration = 0.7F;
        this.startingDuration = 0.7F;
        this.color = Color.WHITE.cpy();
        this.renderBehind = false;
    }

    public void update() {
        super.update();
        this.scale = Interpolation.exp5In.apply(Settings.scale, Settings.scale * 0.3F, this.duration / this.startingDuration);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        if (this.img != null) {
            sb.draw(this.img, this.x - 64.0F, this.y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale * 3.0F, this.scale * 3.0F, 0.0F, 0, 0, 128, 128, false, false);
        }
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {

    }
}

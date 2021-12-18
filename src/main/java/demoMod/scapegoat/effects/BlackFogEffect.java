package demoMod.scapegoat.effects;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBlurEffect;

public class BlackFogEffect extends AbstractGameEffect {
    private float x;
    private float y;

    public BlackFogEffect(float x, float y) {
        this.x = x;
        this.y = y;
        this.duration = 0.2F;
    }

    @Override
    public void update() {
        if (this.duration == 0.2F) {
            CardCrawlGame.sound.play("ATTACK_WHIFF_2");

            for(int i = 0; i < 90; ++i) {
                AbstractGameEffect effect = new SmokeBlurEffect(this.x, this.y);
                Color color = ReflectionHacks.getPrivate(effect, AbstractGameEffect.class, "color");
                color.r = 0;
                color.g = 0;
                color.b = 0;
                color.a = 1;
                AbstractDungeon.effectsQueue.add(effect);
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            CardCrawlGame.sound.play("APPEAR");
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {

    }
}

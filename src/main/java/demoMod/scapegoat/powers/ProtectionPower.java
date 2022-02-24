package demoMod.scapegoat.powers;

import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.GainPowerEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.effects.FlashBigPowerEffect;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ProtectionPower extends AbstractPower {
    public static final String POWER_ID = Scapegoat.makeID("ProtectionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ProtectionPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.img = ImageMaster.loadImage(Scapegoat.getResourcePath("powers/ProtectionPower.png"));
        this.type = AbstractPower.PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType != DamageInfo.DamageType.HP_LOSS) {
            return damage - this.amount;
        }
        return super.atDamageReceive(damage, damageType);
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        if (this.img != null) {
            sb.setColor(c);
            sb.draw(this.img, x - 11.0F, y - 11.0F, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale * 1.5F, Settings.scale * 1.5F, 0.0F, 0, 0, 128, 128, false, false);
        }
    }

    @Override
    public void flash() {
        try {
            Field effect = SuperclassFinder.getSuperclassField(getClass(), "effect");
            effect.setAccessible(true);
            ((ArrayList<GainPowerEffect>)effect.get(this)).add(new GainPowerEffect(this));
            AbstractDungeon.effectList.add(new FlashBigPowerEffect(this));
        } catch (NoSuchFieldException|IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

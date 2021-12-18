package demoMod.scapegoat.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.scapegoat.Scapegoat;

public class PowerRegionLoader {
    public static void loadRegion(AbstractPower power) {
        String name = power.getClass().getSimpleName();
        if (name.endsWith("Power")) {
            name = name.substring(0, name.length() - 5);
        }
        power.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(Scapegoat.getResourcePath(String.format("powers/%s84.png", name))), 0, 0, 84, 84);
        power.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(Scapegoat.getResourcePath(String.format("powers/%s32.png", name))), 0, 0, 32, 32);
    }
}

package demoMod.scapegoat.interfaces;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.function.Supplier;

public interface BetaArtCard {
    String getCardID();

    String getTextureImg();

    String getBetaArtPath();

    Supplier<Texture> getDefaultTexture();

    default Texture getPortraitImage() {
        if (Settings.PLAYTESTER_ART_MODE || UnlockTracker.betaCardPref.getBoolean(this.getCardID(), false)) {
            if (this.getTextureImg() == null) {
                return null;
            } else {
                if (this.getBetaArtPath() != null) {
                    int endingIndex = this.getBetaArtPath().lastIndexOf(".");
                    String newPath = this.getBetaArtPath().substring(0, endingIndex) + "_p" + this.getBetaArtPath().substring(endingIndex);
                    System.out.println("Finding texture: " + newPath);

                    Texture portraitTexture;
                    try {
                        portraitTexture = ImageMaster.loadImage(newPath);
                    } catch (Exception e) {
                        portraitTexture = null;
                    }

                    return portraitTexture;
                }
            }
        }
        return getDefaultTexture().get();
    }
}

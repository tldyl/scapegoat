package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.actions.BurialAction;
import demoMod.scapegoat.actions.RecallAction;

public class BurningPipe extends CustomRelic {
    public static final String ID = Scapegoat.makeID("BurningPipe");
    public static final String IMG_PATH = "relics/burningPipe.png";
    public static final String IMG_OUTLINE_PATH = "relics/burningPipeOutline.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));
    private static final Texture OUTLINE_IMG = new Texture(Scapegoat.getResourcePath(IMG_OUTLINE_PATH));

    public BurningPipe() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void obtain() {
        this.hb.hovered = false;
        int index = 1;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic.relicId.equals(BronzePipe.ID)) break;
            index++;
        }
        this.targetX = 64.0F * Settings.scale * index;
        this.targetY = Settings.isMobile ? (float)Settings.HEIGHT - 132.0F * Settings.scale : (float)Settings.HEIGHT - 102.0F * Settings.scale;
        AbstractDungeon.player.relics.set(index - 1, this);
        this.relicTip();
        UnlockTracker.markRelicAsSeen(this.relicId);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        this.flash();
        addToTop(new RecallAction(2));
        addToTop(new BurialAction(4));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(BronzePipe.ID);
    }
}

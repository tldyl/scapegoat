package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import demoMod.scapegoat.Scapegoat;

import java.util.ArrayList;

public abstract class AbstractAbnormalityPages extends CustomRelic {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(Scapegoat.makeID("AbstractAbnormalityPages"));
    public static final String[] TEXT = uiStrings.TEXT;
    private static final String IMG_PATH = Scapegoat.getResourcePath("relics/EGOPages.png");
    private static final Texture IMG = new Texture(IMG_PATH);

    private boolean RclickStart;
    private boolean Rclick;

    public int[] chooseCard = new int[] { -1 };

    public ArrayList<AbstractCard> cardsToPreview = new ArrayList<>();

    public boolean noPreview = false;

    public AbstractAbnormalityPages(String id, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound landingSound) {
        super(id, IMG, tier, landingSound);
        this.Rclick = false;
        this.RclickStart = false;
    }

    protected void onRightClick() {}

    @Override
    public void onEquip() {
        super.onEquip();
        this.noPreview = true;
    }

    @Override
    public void update() {
        super.update();
        if (this.RclickStart && InputHelper.justReleasedClickRight) {
            if (this.hb.hovered) {
                this.Rclick = true;
            }
            this.RclickStart = false;
        }
        if (this.isObtained && this.hb != null && this.hb.hovered && InputHelper.justClickedRight) {
            this.RclickStart = true;
        }
        if (this.Rclick) {
            this.Rclick = false;
            onRightClick();
        }
    }

    @Override
    public void renderTip(SpriteBatch sb) {
        super.renderTip(sb);
        if (this.chooseCard[0] == -1 && !this.noPreview) {
            renderCardPreview(sb);
        }
    }

    private void renderCardPreview(SpriteBatch sb) {
        float index = 0;
        for (AbstractCard card : cardsToPreview) {
            if (card != null) {
                card.drawScale = 0.6F;
                card.current_x = InputHelper.mX + (index - cardsToPreview.size() / 2.0F) * 200.0F * Settings.scale;
                card.current_y = InputHelper.mY + 140.0F * Settings.scale;
                card.render(sb);
            }
            index++;
        }
    }
}

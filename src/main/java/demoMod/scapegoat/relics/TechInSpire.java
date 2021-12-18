package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import demoMod.scapegoat.Scapegoat;

import java.util.*;
import java.util.stream.Collectors;

public class TechInSpire extends CustomRelic {
    public static final String ID = Scapegoat.makeID("TechInSpire");
    public static final String IMG_PATH = "relics/techInSpire.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));
    private boolean cardsSelected = true;

    public TechInSpire() {
        super(ID, IMG, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        this.cardsSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (AbstractCard card : AbstractDungeon.player.masterDeck.getPurgeableCards().group) {
            tmp.addToTop(card);
        }

        if (tmp.group.isEmpty()) {
            this.cardsSelected = true;
        } else {
            if (tmp.group.size() <= 2) {
                this.deleteCards(tmp.group);
            } else {
                AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), 1, this.DESCRIPTIONS[1], false, false, false, true);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.cardsSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
            this.deleteCards(AbstractDungeon.gridSelectScreen.selectedCards);
        }
    }

    private void deleteCards(ArrayList<AbstractCard> group) {
        this.cardsSelected = true;
        float displayCount = 0.0F;

        for (AbstractCard card : group) {
            card.untip();
            card.unhover();
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, (float) Settings.WIDTH / 3.0F + displayCount, (float) Settings.HEIGHT / 2.0F));
            displayCount += (float) Settings.WIDTH / 6.0F;
            AbstractDungeon.player.masterDeck.removeCard(card);
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        List<AbstractCard> tmp = AbstractDungeon.player.drawPile.group.stream().filter(AbstractCard::canUpgrade).collect(Collectors.toList());
        Collections.shuffle(tmp, new Random(AbstractDungeon.shuffleRng.randomLong()));
        for (int i=0;i<3;i++) {
            if (i >= tmp.size()) break;
            tmp.get(i).superFlash();
            tmp.get(i).upgrade();
            tmp.get(i).applyPowers();
        }
    }
}

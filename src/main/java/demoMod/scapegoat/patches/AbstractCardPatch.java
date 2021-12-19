package demoMod.scapegoat.patches;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class AbstractCardPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
    )
    public static class AddFieldPatch {
        public static SpireField<Boolean> isBottom = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "initializeDeck"
    )
    public static class PatchInitializeDeck {
        public static void Postfix(CardGroup drawPile, CardGroup masterDeck) {
            List<AbstractCard> tmp = new ArrayList<>();
            for (AbstractCard card : drawPile.group) {
                if (AddFieldPatch.isBottom.get(card)) {
                    tmp.add(card);
                }
            }
            drawPile.group.removeAll(tmp);
            drawPile.group.addAll(0, tmp);
        }
    }

    public static class ShamePatch {
        @SpirePatch(
                clz = Shame.class,
                method = SpirePatch.CONSTRUCTOR
        )
        public static class PatchConstructor {
            public static final String ID = Scapegoat.makeID("Shame");
            public static final String NAME = CardCrawlGame.languagePack.getCardStrings(ID).NAME;
            public static final String IMG_PATH = Scapegoat.getResourcePath("cards/shame.png");
            public static final Texture IMG = ImageMaster.loadImage(IMG_PATH);

            @SpireInsertPatch(rloc = 1)
            public static void Insert(Shame card) {
                if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                    card.name = NAME;
                    card.assetUrl = IMG_PATH;
                    Texture cardTexture = IMG;
                    cardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    int tw = cardTexture.getWidth();
                    int th = cardTexture.getHeight();
                    TextureAtlas.AtlasRegion cardImg = new TextureAtlas.AtlasRegion(cardTexture, 0, 0, tw, th);
                    ReflectionHacks.setPrivateInherited(card, CustomCard.class, "portrait", cardImg);
                }
            }
        }
    }

    public static class AscendersBanePatch {
        @SpirePatch(
                clz = AscendersBane.class,
                method = SpirePatch.CONSTRUCTOR
        )
        public static class PatchConstructor {
            public static final String ID = Scapegoat.makeID("AscendersBane");
            public static final String NAME = CardCrawlGame.languagePack.getCardStrings(ID).NAME;
            public static final String IMG_PATH = Scapegoat.getResourcePath("cards/ascendersBane.png");
            public static final Texture IMG = ImageMaster.loadImage(IMG_PATH);

            @SpireInsertPatch(rloc = 1)
            public static void Insert(AscendersBane card) {
                if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                    card.name = NAME;
                    card.assetUrl = IMG_PATH;
                    Texture cardTexture = IMG;
                    cardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    int tw = cardTexture.getWidth();
                    int th = cardTexture.getHeight();
                    TextureAtlas.AtlasRegion cardImg = new TextureAtlas.AtlasRegion(cardTexture, 0, 0, tw, th);
                    ReflectionHacks.setPrivateInherited(card, CustomCard.class, "portrait", cardImg);
                }
            }
        }
    }

    public static class DoubtPatch {
        @SpirePatch(
                clz = Doubt.class,
                method = SpirePatch.CONSTRUCTOR
        )
        public static class PatchConstructor {
            public static final String ID = Scapegoat.makeID("Doubt");
            public static final String NAME = CardCrawlGame.languagePack.getCardStrings(ID).NAME;
            public static final String IMG_PATH = Scapegoat.getResourcePath("cards/doubt.png");
            public static final Texture IMG = ImageMaster.loadImage(IMG_PATH);

            @SpireInsertPatch(rloc = 1)
            public static void Insert(Doubt card) {
                if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                    card.name = NAME;
                    card.assetUrl = IMG_PATH;
                    Texture cardTexture = IMG;
                    cardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    int tw = cardTexture.getWidth();
                    int th = cardTexture.getHeight();
                    TextureAtlas.AtlasRegion cardImg = new TextureAtlas.AtlasRegion(cardTexture, 0, 0, tw, th);
                    ReflectionHacks.setPrivateInherited(card, CustomCard.class, "portrait", cardImg);
                }
            }
        }
    }

    public static class RegretPatch {
        @SpirePatch(
                clz = Regret.class,
                method = SpirePatch.CONSTRUCTOR
        )
        public static class PatchConstructor {
            public static final String ID = Scapegoat.makeID("Regret");
            public static final String NAME = CardCrawlGame.languagePack.getCardStrings(ID).NAME;
            public static final String IMG_PATH = Scapegoat.getResourcePath("cards/regret.png");
            public static final Texture IMG = ImageMaster.loadImage(IMG_PATH);

            @SpireInsertPatch(rloc = 1)
            public static void Insert(Regret card) {
                if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                    card.name = NAME;
                    card.assetUrl = IMG_PATH;
                    Texture cardTexture = IMG;
                    cardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    int tw = cardTexture.getWidth();
                    int th = cardTexture.getHeight();
                    TextureAtlas.AtlasRegion cardImg = new TextureAtlas.AtlasRegion(cardTexture, 0, 0, tw, th);
                    ReflectionHacks.setPrivateInherited(card, CustomCard.class, "portrait", cardImg);
                }
            }
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "loadPortraitImg"
    )
    public static class PatchLoadPortraitImg {
        public static SpireReturn<Void> Prefix(SingleCardViewPopup popup) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                AbstractCard card = ReflectionHacks.getPrivate(popup, SingleCardViewPopup.class, "card");
                if (card.assetUrl != null && !card.assetUrl.equals("status/beta")) {
                    int endingIndex = card.assetUrl.lastIndexOf(".");
                    if (endingIndex == -1) return SpireReturn.Continue();
                    String newPath = card.assetUrl.substring(0, endingIndex) + "_p" + card.assetUrl.substring(endingIndex);
                    Texture portraitImg = ImageMaster.loadImage(newPath);
                    if (portraitImg != null) {
                        ReflectionHacks.setPrivate(popup, SingleCardViewPopup.class, "portraitImg", portraitImg);
                        return SpireReturn.Return(null);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}

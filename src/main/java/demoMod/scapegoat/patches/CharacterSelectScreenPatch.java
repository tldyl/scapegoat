package demoMod.scapegoat.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;

public class CharacterSelectScreenPatch {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(Scapegoat.makeID("Reskin"));
    public static final String[] TEXT = uiStrings.TEXT;

    public static Hitbox reskinRight;
    public static Hitbox reskinLeft;

    private static float reskin_Text_W = FontHelper.getSmartWidth(FontHelper.cardTitleFont, TEXT[1], 9999.0F, 0.0F);

    private static float reskin_W = reskin_Text_W + 200.0f * Settings.scale;
    private static float reskinX_center = 600.0F * Settings.scale;
    public static float allTextInfoX = 0.0f;
    public static int reskinIndex = 0;
    private static final int skinsCount = 2;

    @SpirePatch(
            clz = CharacterSelectScreen.class,
            method = "initialize"
    )
    public static class PatchInitialize {
        public static void Postfix(CharacterSelectScreen screen) {
            reskinRight = new Hitbox(80.0f * Settings.scale, 80.0f * Settings.scale);
            reskinLeft = new Hitbox(80.0f * Settings.scale, 80.0f * Settings.scale);

            reskinRight.move(Settings.WIDTH / 2.0F + reskin_W / 2.0F - reskinX_center + allTextInfoX, 800.0F * Settings.scale);
            reskinLeft.move(Settings.WIDTH / 2.0F - reskin_W / 2.0F - reskinX_center + allTextInfoX, 800.0F * Settings.scale);
        }
    }

    @SpirePatch(
            clz = CharacterSelectScreen.class,
            method = "render"
    )
    public static class PatchRender {
        public static void Postfix(CharacterSelectScreen screen, SpriteBatch sb) {
            if (!Scapegoat.isReskinUnlocked) return;
            for (CharacterOption o : screen.options) {
                if (reskinIndex < 0) reskinIndex = 0;
                if (o.name.equals(ScapegoatCharacter.NAME) && o.selected) {
                    reskinRight.render(sb);
                    reskinLeft.render(sb);

                    //   皮肤选择箭头渲染
                    if (reskinRight.hovered || Settings.isControllerMode) {
                        sb.setColor(Color.WHITE.cpy());
                    } else {
                        sb.setColor(Color.LIGHT_GRAY.cpy());
                    }
                    sb.draw(ImageMaster.CF_RIGHT_ARROW, Settings.WIDTH / 2.0F + reskin_W / 2.0F - reskinX_center - 36.0f * Settings.scale + allTextInfoX, 800.0F * Settings.scale - 36.0f * Settings.scale, 0.0f, 0.0f, 48.0f, 48.0f, Settings.scale * 1.5f, Settings.scale * 1.5f, 0.0F, 0, 0, 48, 48, false, false);
                    if (reskinLeft.hovered || Settings.isControllerMode) {
                        sb.setColor(Color.WHITE.cpy());
                    } else {
                        sb.setColor(Color.LIGHT_GRAY.cpy());
                    }
                    sb.draw(ImageMaster.CF_LEFT_ARROW, Settings.WIDTH / 2.0F - reskin_W / 2.0F - reskinX_center - 36.0f * Settings.scale + allTextInfoX, 800.0F * Settings.scale - 36.0f * Settings.scale, 0.0f, 0.0f, 48.0f, 48.0f, Settings.scale * 1.5f, Settings.scale * 1.5f, 0.0F, 0, 0, 48, 48, false, false);

                    //   皮肤选择字体渲染
                    FontHelper.cardTitleFont.getData().setScale(1.0F);
                    FontHelper.losePowerFont.getData().setScale(0.8F);

                    FontHelper.renderFontCentered(sb, FontHelper.losePowerFont, TEXT[0], Settings.WIDTH / 2.0F - reskinX_center + allTextInfoX, 850.0F * Settings.scale, Settings.GOLD_COLOR.cpy());
                    FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[1 + reskinIndex], Settings.WIDTH / 2.0F - reskinX_center + allTextInfoX, 800.0F * Settings.scale, Settings.GOLD_COLOR.cpy());
                }
            }
        }
    }

    @SpirePatch(
            clz = CharacterSelectScreen.class,
            method = "update"
    )
    public static class PatchUpdate {
        public static void Postfix(CharacterSelectScreen screen) {
            if (!Scapegoat.isReskinUnlocked) return;
            for (CharacterOption o : screen.options) {
                if (reskinIndex < 0) reskinIndex = 0;
                if (o.name.equals(ScapegoatCharacter.NAME) && o.selected) {
                    if (InputHelper.justClickedLeft && reskinLeft.hovered) {
                        reskinLeft.clickStarted = true;
                        CardCrawlGame.sound.play("UI_CLICK_1");
                    }

                    if (InputHelper.justClickedLeft && reskinRight.hovered) {
                        reskinRight.clickStarted = true;
                        CardCrawlGame.sound.play("UI_CLICK_1");
                    }

                    if (reskinLeft.justHovered || reskinRight.justHovered)
                        CardCrawlGame.sound.playV("UI_HOVER", 0.75f);

                    reskinRight.move(Settings.WIDTH / 2.0F + reskin_W / 2.0F - reskinX_center + allTextInfoX, 800.0F * Settings.scale);
                    reskinLeft.move(Settings.WIDTH / 2.0F - reskin_W / 2.0F - reskinX_center + allTextInfoX, 800.0F * Settings.scale);

                    reskinLeft.update();
                    reskinRight.update();

                    if (reskinRight.clicked || CInputActionSet.pageRightViewExhaust.isJustPressed()) {
                        reskinRight.clicked = false;

                        if (reskinIndex < skinsCount - 1) {
                            reskinIndex += 1;
                        } else {
                            reskinIndex = 0;
                        }
                        Scapegoat.reskinSaveData.setInt("reskinIndex", reskinIndex);
                    }

                    if (reskinLeft.clicked || CInputActionSet.pageRightViewExhaust.isJustPressed()) {
                        reskinLeft.clicked = false;

                        if (reskinIndex > 0) {
                            reskinIndex -= 1;
                        } else {
                            reskinIndex = skinsCount - 1;
                        }
                        Scapegoat.reskinSaveData.setInt("reskinIndex", reskinIndex);
                    }
                }
            }
        }
    }
}

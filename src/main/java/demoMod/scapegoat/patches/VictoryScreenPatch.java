package demoMod.scapegoat.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.scene.SilentVictoryStarEffect;
import demoMod.scapegoat.characters.ScapegoatCharacter;

import java.util.ArrayList;

public class VictoryScreenPatch {
    @SpirePatch(
            clz = VictoryScreen.class,
            method = "updateVfx"
    )
    public static class PatchUpdateVfx {
        public static void Postfix(VictoryScreen screen) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(screen, VictoryScreen.class, "effect");
                if (effect.size() < 100) {
                    effect.add(new SilentVictoryStarEffect());
                    effect.add(new SilentVictoryStarEffect());
                    effect.add(new SilentVictoryStarEffect());
                    effect.add(new SilentVictoryStarEffect());
                }
            }
        }
    }

    @SpirePatch(
            clz = VictoryScreen.class,
            method = "render"
    )
    public static class PatchRender {
        @SpireInsertPatch(rloc = 12)
        public static void Insert(VictoryScreen screen, SpriteBatch sb) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                sb.setColor(Color.WHITE);
                AbstractDungeon.player.renderShoulderImg(sb);
            }
        }
    }
}

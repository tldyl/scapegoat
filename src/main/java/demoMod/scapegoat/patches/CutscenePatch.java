package demoMod.scapegoat.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractPlayerEnum;

import java.util.ArrayList;

public class CutscenePatch {
    @SpirePatch(
            clz = Cutscene.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(Cutscene cutscene, AbstractPlayer.PlayerClass chosenClass) {
            if (chosenClass == AbstractPlayerEnum.SCAPEGOAT) {
                ArrayList<CutscenePanel> cutscenePanels = new ArrayList<>();
                Texture bgImg = new Texture("images/scenes/blueBg.jpg");
                cutscenePanels.add(
                        new CutscenePanel(Scapegoat.getResourcePath("scenes/scapegoat1.png"), "ATTACK_HEAVY")
                );
                cutscenePanels.add(
                        new CutscenePanel(Scapegoat.getResourcePath("scenes/scapegoat2.png"))
                );
                cutscenePanels.add(
                        new CutscenePanel(Scapegoat.getResourcePath("scenes/scapegoat3.png"))
                );
                ArrayList<CutscenePanel> panels = ReflectionHacks.getPrivate(cutscene, Cutscene.class, "panels");
                panels.clear();
                panels.addAll(cutscenePanels);
                ReflectionHacks.setPrivate(cutscene, Cutscene.class, "bgImg", bgImg);
            }
        }
    }
}

package demoMod.scapegoat.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.relics.SinAndBloodstain;

@SuppressWarnings("unused")
public class BlightHelperPatch {
    @SpirePatch(
            clz = BlightHelper.class,
            method = "getBlight"
    )
    public static class PatchBlights {
        public static SpireReturn<AbstractBlight> Prefix(String id) {
            if (id.equals(Scapegoat.makeID("SinAndBloodstain"))) {
                return SpireReturn.Return(new SinAndBloodstain());
            }
            return SpireReturn.Continue();
        }
    }
}

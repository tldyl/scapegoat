package demoMod.scapegoat.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import demoMod.scapegoat.monsters.WatcherElite;

public class SaveAndContinuePatch {
    @SpirePatch(
            clz = SaveAndContinue.class,
            method = "loadSaveFile",
            paramtypez = {
                    String.class
            }
    )
    public static class PatchLoadSaveFile {
        public static void Prefix(String filePath) {
            if (AbstractDungeon.currMapNode != null) {
                if (AbstractDungeon.getCurrRoom().monsters != null) {
                    WatcherElite watcherElite = (WatcherElite) AbstractDungeon.getCurrRoom().monsters.getMonster(WatcherElite.ID);
                    if (watcherElite != null) {
                        watcherElite.stance.onExitStance();
                    }
                }
            }
        }
    }
}

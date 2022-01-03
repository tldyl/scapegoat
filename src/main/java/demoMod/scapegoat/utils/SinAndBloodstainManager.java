package demoMod.scapegoat.utils;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import demoMod.scapegoat.interfaces.PostIncreaseBloodstainSubscriber;
import demoMod.scapegoat.interfaces.PostIncreaseSinSubscriber;
import demoMod.scapegoat.relics.SinAndBloodstain;

public class SinAndBloodstainManager {
    private static void checkHasBlight() {
        if (!AbstractDungeon.player.hasBlight(SinAndBloodstain.ID)) {
            AbstractBlight blight = new SinAndBloodstain();
            blight.instantObtain(AbstractDungeon.player, AbstractDungeon.player.blights.size(), true);
        }
    }

    public static void increaseSin(int amount) {
        checkHasBlight();
        if (AbstractDungeon.ascensionLevel >= 15) amount++;
        SinAndBloodstain sinAndBloodstain = (SinAndBloodstain) AbstractDungeon.player.getBlight(SinAndBloodstain.ID);
        sinAndBloodstain.sinCounter += amount;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof PostIncreaseSinSubscriber) {
                ((PostIncreaseSinSubscriber) relic).onIncreaseSin(amount);
            }
        }
    }

    public static void increaseBloodstain(int amount) {
        checkHasBlight();
        if (AbstractDungeon.ascensionLevel >= 15) amount++;
        SinAndBloodstain sinAndBloodstain = (SinAndBloodstain) AbstractDungeon.player.getBlight(SinAndBloodstain.ID);
        sinAndBloodstain.bloodstainCounter += amount;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof PostIncreaseBloodstainSubscriber) {
                ((PostIncreaseBloodstainSubscriber) relic).onIncreaseBloodstain(amount);
            }
        }
    }
}

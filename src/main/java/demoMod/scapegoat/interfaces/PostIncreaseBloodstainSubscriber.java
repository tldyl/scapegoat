package demoMod.scapegoat.interfaces;

import basemod.interfaces.ISubscriber;

public interface PostIncreaseBloodstainSubscriber extends ISubscriber {
    void onIncreaseBloodstain(int amount);
}

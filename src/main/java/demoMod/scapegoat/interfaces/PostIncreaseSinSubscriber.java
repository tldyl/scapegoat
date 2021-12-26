package demoMod.scapegoat.interfaces;

import basemod.interfaces.ISubscriber;

public interface PostIncreaseSinSubscriber extends ISubscriber {
    void onIncreaseSin(int amount);
}

package demoMod.scapegoat.interfaces;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface PostBurialSubscriber extends ISubscriber {
    /**
     * 对于牌来说，表示这张牌被归葬了
     * 对于能力来说，表示发生了一次归葬
     */
    void onBurial();

    /**
     * 为牌准备的表示发生了一次归葬的接口
     */
    default void onBurialForCard() {}

    /**
     * 表示有牌被归葬了
     * @param cardToBurial 被归葬的牌(已经在弃牌堆了)
     */
    default void onBurial(AbstractCard cardToBurial) {
    }
}

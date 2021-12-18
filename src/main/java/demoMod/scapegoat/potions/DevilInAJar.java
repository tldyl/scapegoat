package demoMod.scapegoat.potions;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.powers.DevilInAJarPower;

public class DevilInAJar extends CustomPotion {
    public static final String ID = Scapegoat.makeID("DevilInAJar");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    private static final String NAME = potionStrings.NAME;
    private static final PotionRarity RARITY = PotionRarity.RARE;
    private static final PotionSize POTION_SIZE = PotionSize.BOTTLE;
    private static final PotionColor POTION_COLOR = PotionColor.BLUE;

    public DevilInAJar() {
        super(NAME, ID, RARITY, POTION_SIZE, POTION_COLOR);
        this.isThrown = false;
        this.targetRequired = false;
        this.labOutlineColor = Scapegoat.mainGoatColor;
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "spotsImg", ImageMaster.POTION_BOTTLE_SPOTS);
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = potionStrings.DESCRIPTIONS[0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new DevilInAJarPower(p)));
    }

    @Override
    public int getPotency(int i) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new DevilInAJar();
    }
}

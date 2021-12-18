package demoMod.scapegoat.monsters;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;

import java.util.UUID;

public class BaredSoul extends AbstractMonster {
    public static final String ID = Scapegoat.makeID("BaredSoul");
    public static final String NAME;

    public BaredSoul(float offsetX, float offsetY, int maxHealth) {
        super(NAME, ID + UUID.randomUUID().toString(), maxHealth, -8.0F, 10.0F, 180.0F, 229.0F, Scapegoat.getResourcePath("monsters/baredSoul.png"), offsetX, offsetY);
    }

    @Override
    public void takeTurn() {
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)0, Intent.UNKNOWN);
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
    }
}

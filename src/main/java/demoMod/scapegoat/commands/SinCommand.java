package demoMod.scapegoat.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

import java.util.ArrayList;

public class SinCommand extends ConsoleCommand {
    public SinCommand() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        try {
            int amount = Integer.parseInt(tokens[depth]);
            if (AbstractDungeon.ascensionLevel >= 15) amount--;
            if (amount < 0) {
                DevConsole.log("Invalid Sin amount.");
                return;
            }
            SinAndBloodstainManager.increaseSin(amount);
        } catch (NumberFormatException e) {
            DevConsole.log("Invalid Sin amount.");
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        if (tokens[depth].matches("\\d+")) {
            complete = true;
        }

        return ConsoleCommand.smallNumbers();
    }
}

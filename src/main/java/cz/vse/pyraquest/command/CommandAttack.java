package cz.vse.pyraquest.command;

import cz.vse.pyraquest.domain.Enemy;
import cz.vse.pyraquest.game.GameState;

public class CommandAttack implements ICommand {
    private static final String NAME = "utoc";

    @Override
    public String execute(GameState state, String... parameters) {
        if (parameters.length == 0)
            return state.getTexts().get("attack.error.noParam");

        var enemyNpc = state.getPlayer().getCurrentRoom().getNpc(parameters[0]);
        if (enemyNpc == null)
            return state.getTexts().get("attack.error.notHere");

        if (!(enemyNpc instanceof Enemy enemy))
            return state.getTexts().get("attack.error.notEnemy");
        if (!enemy.isAlive())
            return state.getTexts().get("attack.error.notHere");

        if (!state.getPlayer().getBackpack().contains("nuz")) {
            state.lose();
            return state.getTexts().get("attack.lose." + enemy.getCode());
        }

        enemy.kill();
        return state.getTexts().get("attack.win." + enemy.getCode());
    }

    @Override
    public String getName() {
        return NAME;
    }
}

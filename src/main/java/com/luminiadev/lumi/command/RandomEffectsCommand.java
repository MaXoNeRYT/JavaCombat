package com.luminiadev.lumi.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.tree.node.PlayersNode;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.entity.effect.Effect;
import cn.nukkit.entity.effect.EffectType;
import cn.nukkit.utils.TextFormat;
import com.luminiadev.lumi.ExamplePlugin;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomEffectsCommand extends PluginCommand<ExamplePlugin> {

    private final Random random = new Random();

    public RandomEffectsCommand(ExamplePlugin owner) {
        super("randomeffects", owner);
        this.setDescription("Give to player random effects");
        // Adding command parameters
        this.getCommandParameters().clear();
        this.addCommandParameters("default", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.TARGET, new PlayersNode())
        });
        // Enable param tree parsing (if used new command api)
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        ParamList list = result.getValue();

        List<Player> players = list.getResult(0);
        if (players.isEmpty()) {
            log.addNoTargetMatch().output();
            return 0;
        }

        // Get random effect type and create effect instance
        EffectType effectType = EffectType.get(random.nextInt(1, 31));
        Effect effect = Effect.get(effectType)
                .setAmplifier(random.nextInt(1, 3))
                .setDuration(random.nextInt(20, 1200));

        for (Player player : players) {
            player.addEffect(effect);
        }

        log.addSuccess(TextFormat.GREEN + "Random effect has been given to " + players.size() + " players!").output();
        return 1;
    }
}

package com.luminiadev.lumi.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.tree.node.PlayersNode;
import cn.nukkit.command.tree.node.StringNode;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.item.Item;
import cn.nukkit.item.material.ItemType;
import cn.nukkit.item.material.ItemTypes;
import com.luminiadev.lumi.ExamplePlugin;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExampleGiveCommand extends PluginCommand<ExamplePlugin> {

    private final Random random = new Random();

    public ExampleGiveCommand(ExamplePlugin owner) {
        super("egive", owner);
        this.setDescription("Example give item");
        this.getCommandParameters().clear();
        this.addCommandParameters("default", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.TARGET, new PlayersNode()),
                CommandParameter.newEnum("item", false, CommandEnum.ENUM_ITEM, new StringNode()),
                CommandParameter.newType("count", true, CommandParamType.INT)
        });
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

        String id = list.getResult(1);
        if (!id.startsWith("minecraft:")) {
            id = "minecraft:" + id;
        }

        ItemType type = ItemTypes.get(id);
        Item item = type.createItem(list.getResult(2, 1)); // Creating item with count

        for (Player player : players) {
            player.giveItem(item);
        }

        log.addSuccess("Item " + type.getIdentifier() + " has been given!").output();
        return 1;
    }
}

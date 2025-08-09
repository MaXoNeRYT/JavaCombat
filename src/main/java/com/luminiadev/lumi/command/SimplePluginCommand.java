package com.luminiadev.lumi.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.item.material.ItemTypes;
import cn.nukkit.utils.TextFormat;
import com.luminiadev.lumi.ExamplePlugin;

public class SimplePluginCommand extends PluginCommand<ExamplePlugin> {

    public SimplePluginCommand(ExamplePlugin owner) {
        super("simplecommand", owner);
        this.setDescription("Simple Command");
        this.setPermission("example.simplecommand");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }

        if (sender instanceof Player player) {
            player.giveItem(ItemTypes.DIAMOND.createItem());
            player.sendMessage(TextFormat.AQUA + "You have been given an item created from item type!");
        }

        sender.sendMessage(TextFormat.GREEN + "Command successfully executed!");
        return true;
    }
}

package com.luminiadev.lumi.command;

import cn.nukkit.block.Block;
import cn.nukkit.block.material.BlockType;
import cn.nukkit.block.material.BlockTypes;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.tree.node.StringNode;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.level.Position;
import com.luminiadev.lumi.ExamplePlugin;

import java.util.Map;

public class ExampleSetBlockCommand extends PluginCommand<ExamplePlugin> {

    public ExampleSetBlockCommand(ExamplePlugin owner) {
        super("esetblock", owner);
        this.setDescription("Example set block");
        this.getCommandParameters().clear();
        this.addCommandParameters("default", new CommandParameter[]{
                CommandParameter.newType("position", CommandParamType.POSITION),
                CommandParameter.newEnum("block", false, CommandEnum.ENUM_BLOCK, new StringNode()),
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        ParamList list = result.getValue();

        Position position = list.getResult(0);

        String id = list.getResult(1);
        if (!id.startsWith("minecraft:")) {
            id = "minecraft:" + id;
        }

        BlockType type = BlockTypes.get(id);
        Block block = type.createBlock();

        position.getLevel().setBlock(position, block);
        log.addSuccess("Block " + type.getIdentifier() + " has been set!").output();
        return 1;
    }
}

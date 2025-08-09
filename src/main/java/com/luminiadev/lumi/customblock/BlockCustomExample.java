package com.luminiadev.lumi.customblock;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.types.inventory.creative.CreativeItemCategory;

public class BlockCustomExample extends Block implements CustomBlock {

    @Override
    public String getName() {
        return "Example Block";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, "stone", CreativeItemCategory.ITEMS)
                .name("Example Block")
                .build();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @Override
    public String getIdentifier() {
        return "lumi:example_block";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        player.sendMessage("Activated example block");
        return true;
    }
}

package com.luminiadev.lumi.customitem;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.network.protocol.types.inventory.creative.CreativeItemCategory;
import cn.nukkit.utils.TextFormat;

public class ItemCustomExample extends ItemCustom {

    public ItemCustomExample() {
        super("lumi:example_item", "Example Item", "diamond");
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.simpleBuilder(this, CreativeItemCategory.ITEMS).build();
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        player.sendMessage("Used example item");
        player.sendMessage("Has custom enchantment: " + (this.hasCustomEnchantment("lumi:custom_enchant") ?
                TextFormat.GREEN + "Yes" :
                TextFormat.RED + "No"));
        return true;
    }
}

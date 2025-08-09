package com.luminiadev.lumi;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.plugin.PluginBase;
import com.luminiadev.lumi.command.RandomEffectsCommand;
import com.luminiadev.lumi.command.SimplePluginCommand;
import com.luminiadev.lumi.customblock.BlockCustomExample;
import com.luminiadev.lumi.customenchantment.EnchantmentCustomExample;
import com.luminiadev.lumi.customitem.ItemCustomExample;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ExamplePlugin extends PluginBase {

    @Override
    public void onLoad() {
        log.info("ExamplePlugin loaded");

        Item.registerCustomItem(ItemCustomExample.class).assertOK();
        Block.registerCustomBlock(List.of(BlockCustomExample.class)).assertOK();
        Enchantment.register(new EnchantmentCustomExample(), true).assertOK();
    }

    @Override
    public void onEnable() {
        log.info("ExamplePlugin enabled");

        this.getServer().getCommandMap().register("ExamplePlugin", new RandomEffectsCommand(this));
        this.getServer().getCommandMap().register("ExamplePlugin", new SimplePluginCommand(this));
    }

    @Override
    public void onDisable() {
        log.info("ExamplePlugin disabled");
    }
}
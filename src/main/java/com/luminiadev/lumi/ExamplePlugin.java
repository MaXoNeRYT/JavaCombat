package com.luminiadev.lumi;

import cn.nukkit.plugin.PluginBase;
import com.luminiadev.lumi.command.RandomEffectsCommand;
import com.luminiadev.lumi.command.SimplePluginCommand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExamplePlugin extends PluginBase {

    @Override
    public void onLoad() {
        log.info("ExamplePlugin loaded");
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
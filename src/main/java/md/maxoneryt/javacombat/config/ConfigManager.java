package md.maxoneryt.javacombat.config;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import lombok.Getter;
import md.maxoneryt.javacombat.JavaCombat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ConfigManager {

    private final JavaCombat plugin;
    private PluginConfig pluginConfig;

    public ConfigManager(JavaCombat plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        Config config = new Config(configFile, Config.YAML);

        PluginConfig pluginConfig = new PluginConfig(
                config.getInt("default-cooldown-ticks", 5),
                config.getDouble("full-damage", 1.0),
                config.getDouble("cooldown-damage-multiplier", 0.5),
                config.getString("cooldown-text-format", "!jc.{0}"),
                config.getDouble("max-cooldown-value", 80.00)
        );

        Map<String, Integer> itemCooldowns = new HashMap<>();
        Map<String, Object> itemCooldownsConfig = config.get("item-cooldowns", new HashMap<String, Object>());
        for (Map.Entry<String, Object> entry : itemCooldownsConfig.entrySet()) {
            itemCooldowns.put(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
        }
        pluginConfig.setItemCooldowns(itemCooldowns);

        Map<String, Integer> tagCooldowns = new HashMap<>();
        Map<String, Object> tagCooldownsConfig = config.get("tag-cooldowns", new HashMap<String, Object>());
        for (Map.Entry<String, Object> entry : tagCooldownsConfig.entrySet()) {
            tagCooldowns.put(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
        }
        pluginConfig.setTagCooldowns(tagCooldowns);

        Map<String, String> itemAnimations = new HashMap<>();
        Map<String, Object> itemAnimationsConfig = config.get("item-animations", new HashMap<String, Object>());
        for (Map.Entry<String, Object> entry : itemAnimationsConfig.entrySet()) {
            itemAnimations.put(entry.getKey(), entry.getValue().toString());
        }
        pluginConfig.setItemAnimations(itemAnimations);

        Map<String, String> tagAnimations = new HashMap<>();
        Map<String, Object> tagAnimationsConfig = config.get("tag-animations", new HashMap<String, Object>());
        for (Map.Entry<String, Object> entry : tagAnimationsConfig.entrySet()) {
            tagAnimations.put(entry.getKey(), entry.getValue().toString());
        }
        pluginConfig.setTagAnimations(tagAnimations);

        Map<String, Integer> axeDamageValues = new HashMap<>();
        Map<String, Object> axeDamageConfig = config.get("axe-damage", new HashMap<String, Object>());
        for (Map.Entry<String, Object> entry : axeDamageConfig.entrySet()) {
            axeDamageValues.put(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
        }
        pluginConfig.setAxeDamageValues(axeDamageValues);

        pluginConfig.setSweepAttackEnabled(config.getBoolean("sweep-attack.enabled", true));
        pluginConfig.setSweepDamageMultiplier(config.getDouble("sweep-attack.damage-multiplier", 1.0));
        pluginConfig.setSweepAttackRange(config.getDouble("sweep-attack.range", 2.5));
        pluginConfig.setSweepParticleCount(config.getInt("sweep-attack.particle-count", 8));

        pluginConfig.setCriticalHitEnabled(config.getBoolean("critical-hit.enabled", true));
        pluginConfig.setCriticalHitMultiplier(config.getDouble("critical-hit.multiplier", 1.5));
        pluginConfig.setCriticalHitRequireAir(config.getBoolean("critical-hit.require-air", true));
        pluginConfig.setCriticalHitCheckLiquid(config.getBoolean("critical-hit.check-liquid", true));
        pluginConfig.setCriticalHitCheckClimbable(config.getBoolean("critical-hit.check-climbable", true));
        pluginConfig.setCriticalHitCheckBlocking(config.getBoolean("critical-hit.check-blocking", true));

        this.pluginConfig = pluginConfig;
    }

    public void saveConfig() {
        Config config = new Config(new File(plugin.getDataFolder(), "config.yml"), Config.YAML);

        config.set("default-cooldown-ticks", pluginConfig.getDefaultCooldownTicks());
        config.set("full-damage", pluginConfig.getFullDamage());
        config.set("cooldown-damage-multiplier", pluginConfig.getCooldownDamageMultiplier());
        config.set("cooldown-text-format", pluginConfig.getCooldownTextFormat());
        config.set("max-cooldown-value", pluginConfig.getMaxCooldownValue());

        config.set("item-cooldowns", pluginConfig.getItemCooldowns());
        config.set("tag-cooldowns", pluginConfig.getTagCooldowns());
        config.set("item-animations", pluginConfig.getItemAnimations());
        config.set("tag-animations", pluginConfig.getTagAnimations());
        config.set("axe-damage", pluginConfig.getAxeDamageValues());

        config.set("sweep-attack.enabled", pluginConfig.isSweepAttackEnabled());
        config.set("sweep-attack.damage-multiplier", pluginConfig.getSweepDamageMultiplier());
        config.set("sweep-attack.range", pluginConfig.getSweepAttackRange());
        config.set("sweep-attack.particle-count", pluginConfig.getSweepParticleCount());

        config.set("critical-hit.enabled", pluginConfig.isCriticalHitEnabled());
        config.set("critical-hit.multiplier", pluginConfig.getCriticalHitMultiplier());
        config.set("critical-hit.require-air", pluginConfig.isCriticalHitRequireAir());
        config.set("critical-hit.check-liquid", pluginConfig.isCriticalHitCheckLiquid());
        config.set("critical-hit.check-climbable", pluginConfig.isCriticalHitCheckClimbable());
        config.set("critical-hit.check-blocking", pluginConfig.isCriticalHitCheckBlocking());

        config.save();
    }
}
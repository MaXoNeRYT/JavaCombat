package md.maxoneryt.javacombat.combat.cooldown;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import lombok.RequiredArgsConstructor;
import md.maxoneryt.javacombat.config.PluginConfig;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class CooldownManager {

    private final PluginConfig config;
    private final CooldownDisplayManager displayManager;
    private final Map<Player, CooldownData> playerCooldowns = new ConcurrentHashMap<>();

    private static class CooldownData {
        int ticks;
        int totalTicks;

        CooldownData(int ticks, int totalTicks) {
            this.ticks = ticks;
            this.totalTicks = totalTicks;
        }
    }

    public void startCooldown(Player player, int ticks) {
        if (config == null || player == null || !player.isOnline()) return;
        playerCooldowns.put(player, new CooldownData(ticks, ticks));
    }

    public double getCurrentCooldown(Player player) {
        if (config == null) return 0;

        CooldownData data = playerCooldowns.get(player);
        if (data == null || data.ticks <= 0) {
            return 0;
        }

        double progress = 1.0 - ((double) data.ticks / data.totalTicks);
        return progress * config.getMaxCooldownValue();
    }

    public void updateCooldowns() {
        if (config == null) return;

        Iterator<Map.Entry<Player, CooldownData>> iterator = playerCooldowns.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Player, CooldownData> entry = iterator.next();
            Player player = entry.getKey();

            if (player == null || !player.isOnline()) {
                iterator.remove();
                displayManager.clearCooldownDisplay(player);
                continue;
            }

            CooldownData data = entry.getValue();
            data.ticks--;

            if (data.ticks <= 0) {
                displayManager.clearCooldownDisplay(player);
                iterator.remove();
            } else {
                double cooldownValue = getCurrentCooldown(player);
                displayManager.updateCooldownDisplay(player, cooldownValue, null);
            }
        }
    }

    public void cleanup() {
        for (Player player : playerCooldowns.keySet()) {
            displayManager.clearCooldownDisplay(player);
        }
        playerCooldowns.clear();
    }

    public int getCooldownForItem(Item item) {
        if (config == null || item == null) return config.getDefaultCooldownTicks();

        String itemId = item.getNamespaceId();

        Integer cooldown = config.getItemCooldowns().get(itemId);
        if (cooldown != null) {
            return cooldown;
        }

        if (itemId.contains("hoe")) {
            Integer hoeCooldown = config.getTagCooldowns().get("minecraft:is_hoe");
            if (hoeCooldown != null) return hoeCooldown;
        } else if (itemId.contains("sword")) {
            Integer swordCooldown = config.getTagCooldowns().get("minecraft:is_sword");
            if (swordCooldown != null) return swordCooldown;
        } else if (itemId.contains("pickaxe")) {
            Integer pickaxeCooldown = config.getTagCooldowns().get("minecraft:is_pickaxe");
            if (pickaxeCooldown != null) return pickaxeCooldown;
        } else if (itemId.contains("shovel")) {
            Integer shovelCooldown = config.getTagCooldowns().get("minecraft:is_shovel");
            if (shovelCooldown != null) return shovelCooldown;
        } else if (itemId.contains("axe")) {
            Integer axeCooldown = config.getTagCooldowns().get("minecraft:is_axe");
            if (axeCooldown != null) return axeCooldown;
        }

        return config.getDefaultCooldownTicks();
    }

    public void handleItemChange(Player player, Item newItem) {
        if (config == null || player == null || !player.isOnline()) return;

        int cooldownTicks = getCooldownForItem(newItem);
        startCooldown(player, cooldownTicks);
    }

    public String getAnimationForItem(Item item) {
        if (config == null || item == null) return "animation.player.hand";

        String itemId = item.getNamespaceId();

        String animation = config.getItemAnimations().get(itemId);
        if (animation != null) {
            return animation;
        }

        if (itemId.contains("hoe")) {
            String hoeAnimation = config.getTagAnimations().get("minecraft:is_hoe");
            if (hoeAnimation != null) return hoeAnimation;
        } else if (itemId.contains("sword")) {
            String swordAnimation = config.getTagAnimations().get("minecraft:is_sword");
            if (swordAnimation != null) return swordAnimation;
        } else if (itemId.contains("pickaxe")) {
            String pickaxeAnimation = config.getTagAnimations().get("minecraft:is_pickaxe");
            if (pickaxeAnimation != null) return pickaxeAnimation;
        } else if (itemId.contains("shovel")) {
            String shovelAnimation = config.getTagAnimations().get("minecraft:is_shovel");
            if (shovelAnimation != null) return shovelAnimation;
        } else if (itemId.contains("axe")) {
            String axeAnimation = config.getTagAnimations().get("minecraft:is_axe");
            if (axeAnimation != null) return axeAnimation;
        }

        return "animation.player.hand";
    }
}
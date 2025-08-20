package md.maxoneryt.javacombat.combat.cooldown;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import lombok.RequiredArgsConstructor;
import md.maxoneryt.javacombat.config.PluginConfig;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CooldownDisplayManager {

    private final PluginConfig config;
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final Map<Player, Entity> playerTargets = new HashMap<>();
    private final Map<Player, String> lastDisplayedText = new HashMap<>();
    private final Map<Player, Boolean> activeCooldowns = new HashMap<>();

    public void updateCooldownDisplay(Player player, double cooldownValue, Entity target) {
        if (config == null || player == null || !player.isOnline()) return;

        boolean hasCooldown = cooldownValue > 0;
        activeCooldowns.put(player, hasCooldown);

        if (hasCooldown) {
            playerTargets.remove(player);
            String displayText = getCooldownText(cooldownValue);
            displayTitle(player, displayText);
            return;
        }

        if (target != null) {
            playerTargets.put(player, target);
        } else {
            playerTargets.remove(player);
        }

        if (hasValidTarget(player)) {
            String maxValueText = getMaxCooldownText();
            displayTitle(player, maxValueText);
        } else {
            clearCooldownDisplay(player);
        }
    }

    public boolean hasActiveCooldown(Player player) {
        return activeCooldowns.getOrDefault(player, false);
    }

    private String getCooldownText(double cooldownValue) {
        String formattedValue = decimalFormat.format(cooldownValue);
        return config.getCooldownTextFormat().replace("{0}", formattedValue);
    }

    private String getMaxCooldownText() {
        String maxValue = decimalFormat.format(config.getMaxCooldownValue());
        return config.getCooldownTextFormat().replace("{0}", maxValue);
    }

    public void clearCooldownDisplay(Player player) {
        if (player == null || !player.isOnline()) return;

        playerTargets.remove(player);
        lastDisplayedText.remove(player);
        activeCooldowns.remove(player);
        player.sendTitle("", "", 0, 0, 0);
    }

    private boolean hasValidTarget(Player player) {
        Entity target = playerTargets.get(player);
        return target != null && !target.isClosed() && target.isAlive() &&
                player.distance(target) <= 5;
    }

    private void displayTitle(Player player, String text) {
        if (text.isEmpty()) {
            clearCooldownDisplay(player);
            return;
        }

        String lastText = lastDisplayedText.get(player);
        if (lastText != null && lastText.equals(text)) {
            return;
        }

        player.sendTitle(text, "", 0, 5, 0);
        lastDisplayedText.put(player, text);
    }

    public void updateAllDisplays() {
        if (config == null) return;

        for (Player player : playerTargets.keySet()) {
            if (player != null && player.isOnline()) {
                Entity target = playerTargets.get(player);

                if (target == null || target.isClosed() || !target.isAlive() ||
                        player.distance(target) > 5) {
                    clearCooldownDisplay(player);
                    continue;
                }

                if (hasActiveCooldown(player)) {
                    continue;
                }

                String maxValueText = getMaxCooldownText();
                displayTitle(player, maxValueText);
            }
        }
    }

    public void cleanup() {
        for (Player player : playerTargets.keySet()) {
            if (player != null && player.isOnline()) {
                player.sendTitle("", "", 0, 0, 0);
            }
        }
        playerTargets.clear();
        lastDisplayedText.clear();
        activeCooldowns.clear();
    }
}
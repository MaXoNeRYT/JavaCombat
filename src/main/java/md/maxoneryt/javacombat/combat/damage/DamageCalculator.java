package md.maxoneryt.javacombat.combat.damage;

import md.maxoneryt.javacombat.config.PluginConfig;

public class DamageCalculator {

    public double calculateCooldownDamage(float originalDamage, double cooldownValue, PluginConfig config) {
        double damageMultiplier = 1.0 - (cooldownValue / config.getMaxCooldownValue());
        damageMultiplier = Math.max(damageMultiplier, config.getCooldownDamageMultiplier());
        return originalDamage * damageMultiplier;
    }

    public float getAxeDamage(String itemId, PluginConfig config) {
        Integer customDamage = config.getAxeDamageValues().get(itemId);
        if (customDamage != null) {
            return customDamage.floatValue();
        }

        return switch (itemId) {
            case "minecraft:wooden_axe" -> 7.0f;
            case "minecraft:stone_axe" -> 9.0f;
            case "minecraft:iron_axe" -> 9.0f;
            case "minecraft:golden_axe" -> 7.0f;
            case "minecraft:diamond_axe" -> 9.0f;
            case "minecraft:netherite_axe" -> 10.0f;
            default -> 0f;
        };
    }
}
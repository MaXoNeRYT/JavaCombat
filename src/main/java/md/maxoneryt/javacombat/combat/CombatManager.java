package md.maxoneryt.javacombat.combat;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import lombok.Getter;
import md.maxoneryt.javacombat.JavaCombat;
import md.maxoneryt.javacombat.combat.animation.AnimationManager;
import md.maxoneryt.javacombat.combat.cooldown.CooldownDisplayManager;
import md.maxoneryt.javacombat.combat.cooldown.CooldownManager;
import md.maxoneryt.javacombat.combat.damage.CriticalHitManager;
import md.maxoneryt.javacombat.combat.damage.DamageCalculator;
import md.maxoneryt.javacombat.combat.particle.ParticleManager;
import md.maxoneryt.javacombat.combat.sweep.SweepAttackManager;
import md.maxoneryt.javacombat.combat.util.ItemChangeDetector;
import md.maxoneryt.javacombat.config.PluginConfig;

@Getter
public class CombatManager {

    private final JavaCombat plugin;
    private final CooldownManager cooldownManager;
    private final DamageCalculator damageCalculator;
    private final AnimationManager animationManager;
    private final SweepAttackManager sweepAttackManager;
    private final CriticalHitManager criticalHitManager;
    private final CooldownDisplayManager displayManager;

    public CombatManager(JavaCombat plugin) {
        this.plugin = plugin;
        PluginConfig config = plugin.getConfigManager().getPluginConfig();

        this.displayManager = new CooldownDisplayManager(config);
        this.cooldownManager = new CooldownManager(config, displayManager);
        this.damageCalculator = new DamageCalculator();
        this.animationManager = new AnimationManager();
        this.sweepAttackManager = new SweepAttackManager(new ParticleManager());
        this.criticalHitManager = new CriticalHitManager(config);
    }

    public void handlePlayerAttack(Player player, EntityDamageByEntityEvent event) {
        PluginConfig config = plugin.getConfigManager().getPluginConfig();
        if (config == null) return;

        Entity target = event.getEntity();
        Item weapon = player.getInventory().getItemInHand();
        int cooldownTicks = cooldownManager.getCooldownForItem(weapon);

        double cooldownValue = cooldownManager.getCurrentCooldown(player);
        boolean isOnCooldown = cooldownValue > 0;

        displayManager.updateCooldownDisplay(player, cooldownValue, target);

        float damage = event.getDamage();
        float finalDamage = damage;

        boolean isCriticalHit = false;
        if (config.isCriticalHitEnabled() && criticalHitManager.canCriticalHit(player, target)) {
            isCriticalHit = true;
        }

        if (isAxe(weapon)) {
            String itemId = weapon.getNamespaceId();
            float axeDamage = damageCalculator.getAxeDamage(itemId, config);
            if (axeDamage > 0) {
                finalDamage = axeDamage;
                event.setDamage(finalDamage);
            }
        }

        if (isOnCooldown) {
            finalDamage = (float) damageCalculator.calculateCooldownDamage(
                    finalDamage,
                    cooldownValue,
                    config
            );
            event.setDamage(finalDamage);
        } else {
            if (sweepAttackManager.isSword(weapon)) {
                sweepAttackManager.performSweepAttack(player, event, config);
            }

            String animationName = cooldownManager.getAnimationForItem(weapon);
            animationManager.playAttackAnimation(player, animationName);
        }

        if (isCriticalHit) {
            criticalHitManager.applyCriticalHit(event, config);
        }

        cooldownManager.startCooldown(player, cooldownTicks);
    }

    private boolean isAxe(Item item) {
        if (item == null) return false;
        String itemId = item.getNamespaceId();
        return itemId.contains("axe") || item.isAxe();
    }

    public void handleAirSwing(Player player) {
        if (player == null || !player.isOnline()) return;

        Item weapon = player.getInventory().getItemInHand();
        if (weapon == null) return;

        int cooldownTicks = cooldownManager.getCooldownForItem(weapon);
        cooldownManager.startCooldown(player, cooldownTicks);

        double currentCooldown = cooldownManager.getCurrentCooldown(player);
        displayManager.updateCooldownDisplay(player, currentCooldown, null);

        String animationName = cooldownManager.getAnimationForItem(weapon);
        animationManager.playAttackAnimation(player, animationName);
    }

    public void handleInventorySlotChange(Player player, Item previousItem, Item newItem) {
        if (player == null || !player.isOnline()) return;

        if (ItemChangeDetector.isRealItemChange(previousItem, newItem)) {
            cooldownManager.handleItemChange(player, newItem);
        }
    }

    public void cleanup() {
        cooldownManager.cleanup();
        animationManager.cleanup();
        displayManager.cleanup();
    }
}
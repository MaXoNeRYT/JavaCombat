package md.maxoneryt.javacombat.combat.damage;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLadder;
import cn.nukkit.block.BlockVine;
import cn.nukkit.block.BlockSnow;
import cn.nukkit.block.BlockWater;
import cn.nukkit.block.BlockLava;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import lombok.RequiredArgsConstructor;
import md.maxoneryt.javacombat.config.PluginConfig;

@RequiredArgsConstructor
public class CriticalHitManager {

    private final PluginConfig config;

    public boolean canCriticalHit(Player attacker, Entity target) {
        if (attacker == null || target == null) return false;

        if (attacker.onGround) return false;

        if (isOnClimbable(attacker)) return false;

        if (isInLiquid(attacker)) return false;

        if (isBlockingAttack(attacker)) return false;

        if (isOnSnow(attacker)) return false;

        return true;
    }

    public void applyCriticalHit(EntityDamageByEntityEvent event, PluginConfig config) {
        float originalDamage = event.getDamage();
        float criticalDamage = calculateCriticalDamage(originalDamage, config);
        event.setDamage(criticalDamage);
    }

    public float calculateCriticalDamage(float originalDamage, PluginConfig config) {
        double multiplier = config.getCriticalHitMultiplier();
        return (float) (originalDamage * multiplier);
    }

    private boolean isOnClimbable(Player player) {
        Block blockUnder = player.getLevel().getBlock(player.getFloorX(), player.getFloorY(), player.getFloorZ());
        return blockUnder instanceof BlockLadder || blockUnder instanceof BlockVine;
    }

    private boolean isInLiquid(Player player) {
        Block blockIn = player.getLevel().getBlock(player.getFloorX(), player.getFloorY(), player.getFloorZ());
        return blockIn instanceof BlockWater || blockIn instanceof BlockLava;
    }

    private boolean isBlockingAttack(Player player) {
        return player.isBlocking();
    }

    private boolean isOnSnow(Player player) {
        Block blockUnder = player.getLevel().getBlock(player.getFloorX(), player.getFloorY() - 1, player.getFloorZ());
        return blockUnder instanceof BlockSnow;
    }
}
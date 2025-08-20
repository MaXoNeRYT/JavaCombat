package md.maxoneryt.javacombat.combat.sweep;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import lombok.RequiredArgsConstructor;
import md.maxoneryt.javacombat.combat.particle.ParticleManager;
import md.maxoneryt.javacombat.config.PluginConfig;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SweepAttackManager {

    private final ParticleManager particleManager;
    private final ThreadLocal<Boolean> isPerformingSweepAttack = ThreadLocal.withInitial(() -> false);

    public void performSweepAttack(Player attacker, EntityDamageByEntityEvent originalEvent, PluginConfig config) {
        if (isPerformingSweepAttack()) {
            return;
        }

        if (!config.isSweepAttackEnabled()) {
            return;
        }

        Item weapon = attacker.getInventory().getItemInHand();
        if (weapon == null || !isSword(weapon)) {
            return;
        }

        Entity mainTarget = originalEvent.getEntity();
        if (mainTarget == null) return;

        float mainDamage = originalEvent.getDamage();
        double radius = config.getSweepAttackRange();

        Vector3 center = mainTarget.getLocation().add(0, mainTarget.getHeight() * 0.8, 0);

        Level level = mainTarget.getLevel();
        List<EntityLiving> nearby = new ArrayList<>();

        for (Entity e : level.getEntities()) {
            if (e == attacker || e == mainTarget) continue;
            if (!(e instanceof EntityLiving)) continue;
            if (e.isClosed() || !e.isAlive()) continue;

            double distance = e.getLocation().add(0, e.getHeight() * 0.5, 0).distance(center);
            if (distance <= radius) {
                nearby.add((EntityLiving) e);
            }
        }

        float sweepDamage = 1f + (mainDamage * 0.5f);

        isPerformingSweepAttack.set(true);

        try {
            if (!nearby.isEmpty()) {
                for (EntityLiving entity : nearby) {
                    if (entity.isClosed() || !entity.isAlive()) continue;

                    EntityDamageEvent damageEvent = new EntityDamageByEntityEvent(
                            attacker,
                            entity,
                            EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                            sweepDamage
                    );

                    entity.attack(damageEvent);
                }
            }
        } finally {
            isPerformingSweepAttack.set(false);
        }

        Position particlePos = new Position(center.x, center.y, center.z, mainTarget.getLevel());
        particleManager.spawnParticle(particlePos, "sweep_attack", config.getSweepParticleCount());
    }

    public boolean isSword(Item item) {
        String itemId = item.getNamespaceId();
        return itemId.contains("sword") ||
                itemId.contains("_sword") ||
                item.isSword();
    }

    public boolean isPerformingSweepAttack() {
        return isPerformingSweepAttack.get();
    }
}
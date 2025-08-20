package md.maxoneryt.javacombat.utils;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.Task;
import md.maxoneryt.javacombat.combat.cooldown.CooldownDisplayManager;

import java.util.HashMap;
import java.util.Map;

public class EntityTargetNotifier {

    private final Plugin plugin;
    private final Map<Player, Entity> lastLookedMobs = new HashMap<>();
    private final Map<Player, Integer> lookTimeCounters = new HashMap<>();
    private final CooldownDisplayManager cooldownDisplayManager;

    public EntityTargetNotifier(Plugin plugin, CooldownDisplayManager displayManager) {
        this.plugin = plugin;
        this.cooldownDisplayManager = displayManager;
        startGlobalTracking();
    }

    private void startGlobalTracking() {
        plugin.getServer().getScheduler().scheduleRepeatingTask(plugin, new Task() {
            @Override
            public void onRun(int currentTick) {
                for (Player player : plugin.getServer().getOnlinePlayers().values()) {
                    checkPlayerLookingAtMob(player);
                }
            }
        }, 4);
    }

    private void checkPlayerLookingAtMob(Player player) {
        if (cooldownDisplayManager != null && cooldownDisplayManager.hasActiveCooldown(player)) {
            clearMobTracking(player);
            return;
        }

        Entity mob = getLookingAtMob(player);

        if (mob != null && mob instanceof EntityLiving) {
            handleMobLook(player, mob);
        } else {
            clearMobTracking(player);
        }
    }

    private void handleMobLook(Player player, Entity mob) {
        Entity lastMob = lastLookedMobs.get(player);

        if (lastMob == null || !lastMob.equals(mob)) {
            resetMobTracking(player, mob);
            showMobTitle(player, mob);
            return;
        }

        int lookTime = lookTimeCounters.getOrDefault(player, 0) + 1;
        lookTimeCounters.put(player, lookTime);

        if (lookTime % 2 == 0) {
            showMobTitle(player, mob);
        }
    }

    private void showMobTitle(Player player, Entity mob) {
        player.sendTitle("!jc.81", "", 0, 0, 0);
    }

    private void resetMobTracking(Player player, Entity mob) {
        lookTimeCounters.put(player, 0);
        lastLookedMobs.put(player, mob);
    }

    private void clearMobTracking(Player player) {
        Entity lastMob = lastLookedMobs.get(player);
        if (lastMob != null) {
            player.sendTitle("", "", 0, 0, 0);
        }
        lastLookedMobs.remove(player);
        lookTimeCounters.remove(player);
    }

    private Entity getLookingAtMob(Player player) {
        if (!player.isOnline()) return null;

        for (Entity entity : player.getLevel().getEntities()) {
            if (!(entity instanceof EntityLiving)) continue;
            if (entity.equals(player)) continue;
            if (isLookingAt(player, entity)) {
                return entity;
            }
        }
        return null;
    }

    private boolean isLookingAt(Player player, Entity entity) {
        if (player.distance(entity) > 4) return false;

        cn.nukkit.math.Vector3 eyePos = new cn.nukkit.math.Vector3(
                player.getX(),
                player.getY() + player.getEyeHeight(),
                player.getZ()
        );

        cn.nukkit.math.Vector3 lookDirection = player.getDirectionVector();
        cn.nukkit.math.Vector3 endPos = eyePos.add(lookDirection.multiply(10));

        cn.nukkit.math.AxisAlignedBB entityBB = entity.getBoundingBox();
        if (entityBB == null) return false;

        return entityBB.calculateIntercept(eyePos, endPos) != null;
    }

    public void cleanup() {
        for (Player player : lastLookedMobs.keySet()) {
            if (player != null && player.isOnline()) {
                player.sendTitle("", "", 0, 0, 0);
            }
        }
        lastLookedMobs.clear();
        lookTimeCounters.clear();
    }
}
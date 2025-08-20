package md.maxoneryt.javacombat.event;

import cn.nukkit.Player;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;
import lombok.RequiredArgsConstructor;
import md.maxoneryt.javacombat.JavaCombat;

@RequiredArgsConstructor
public class CombatEventSubscriber {

    private final JavaCombat plugin;
    private Item lastHeldItem = null;

    public void registerEvents() {
        plugin.getServer().getPluginManager().subscribeEvent(
                EntityDamageByEntityEvent.class,
                this::onEntityDamageByEntity,
                EventPriority.NORMAL,
                plugin
        );

        plugin.getServer().getPluginManager().subscribeEvent(
                InventoryTransactionEvent.class,
                this::onInventoryTransaction,
                EventPriority.NORMAL,
                plugin
        );

        plugin.getServer().getPluginManager().subscribeEvent(
                PlayerInteractEvent.class,
                this::onPlayerInteract,
                EventPriority.NORMAL,
                plugin
        );

        plugin.getServer().getPluginManager().subscribeEvent(
                PlayerItemHeldEvent.class,
                this::onPlayerItemHeld,
                EventPriority.NORMAL,
                plugin
        );

        startCooldownUpdateTask();
    }

    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (plugin.getCombatManager().getSweepAttackManager().isPerformingSweepAttack()) {
            return;
        }

        if (event.getDamager() instanceof cn.nukkit.Player player) {
            plugin.getCombatManager().handlePlayerAttack(player, event);
        }
    }

    private void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Item newItem = event.getItem();
        Item previousItem = player.getInventory().getItemInHand();

        plugin.getCombatManager().handleInventorySlotChange(player, previousItem, newItem);
    }

    private void onInventoryTransaction(InventoryTransactionEvent event) {
        if (!(event.getTransaction().getSource() instanceof Player)) return;

        Player player = (Player) event.getTransaction().getSource();
        Item currentItem = player.getInventory().getItemInHand();

        for (var action : event.getTransaction().getActions()) {
            if (action instanceof SlotChangeAction) {
                Item previousItem = ((SlotChangeAction) action).getSourceItem();
                plugin.getCombatManager().handleInventorySlotChange(player, previousItem, currentItem);
                break;
            }
        }
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_AIR) {
            plugin.getCombatManager().handleAirSwing(event.getPlayer());
        }
    }

    private void startCooldownUpdateTask() {
        plugin.getServer().getScheduler().scheduleDelayedTask(new Task() {
            @Override
            public void onRun(int currentTick) {
                if (plugin.getConfigManager().getPluginConfig() != null) {
                    plugin.getServer().getScheduler().scheduleRepeatingTask(new Task() {
                        @Override
                        public void onRun(int currentTick) {
                            plugin.getCombatManager().getCooldownManager().updateCooldowns();
                        }
                    }, 1);
                }
            }
        }, 20);
    }
}
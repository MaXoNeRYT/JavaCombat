package md.maxoneryt.javacombat.event;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.player.PlayerAnimationEvent;
import cn.nukkit.network.protocol.AnimatePacket;
import lombok.RequiredArgsConstructor;
import md.maxoneryt.javacombat.JavaCombat;

@RequiredArgsConstructor
public class AirSwingEventSubscriber {


    private final JavaCombat plugin;

    public void registerEvents() {
        plugin.getServer().getPluginManager().subscribeEvent(
                PlayerAnimationEvent.class,
                this::onAnimation,
                EventPriority.NORMAL,
                plugin
        );
    }

    private void onAnimation(PlayerAnimationEvent event) {
        if (event.getAnimationType() != AnimatePacket.Action.SWING_ARM) {
            return;
        }

        Player player = event.getPlayer();

        Block target = player.getTargetBlock(5);
        if (target != null && target.getId() != Block.AIR) {
            return;
        }

        plugin.getCombatManager().handleAirSwing(player);
    }
}
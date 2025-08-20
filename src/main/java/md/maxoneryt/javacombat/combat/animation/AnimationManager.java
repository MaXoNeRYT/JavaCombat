package md.maxoneryt.javacombat.combat.animation;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.network.protocol.AnimateEntityPacket;

import java.util.Collections;

public class AnimationManager {

    public void playAttackAnimation(Player player, String animationName) {
        if (player == null || animationName == null || animationName.isEmpty()) return;

        playCustomAnimation(player, animationName);
    }

    private void playCustomAnimation(Player player, String animationName) {
        try {
            var animationBuilder = AnimateEntityPacket.Animation.builder();

            animationBuilder.animation(animationName);

            animationBuilder.nextState("default"); // DEFAULT_NEXT_STATE
            animationBuilder.blendOutTime(0.0f);   // DEFAULT_BLEND_OUT_TIME
            animationBuilder.stopExpression("query.any_animation_finished"); // DEFAULT_STOP_EXPRESSION
            animationBuilder.controller("__runtime_controller"); // DEFAULT_CONTROLLER
            animationBuilder.stopExpressionVersion(16777216); // DEFAULT_STOP_EXPRESSION_VERSION

            Entity.playAnimationOnEntities(animationBuilder.build(), Collections.singletonList(player));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanup() {
        // А ничо и Шо?
    }
}
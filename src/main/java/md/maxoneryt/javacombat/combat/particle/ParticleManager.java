package md.maxoneryt.javacombat.combat.particle;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

public class ParticleManager {

    public void spawnParticle(Position position, String particleName, int count) {
        if (count < 1) count = 1;

        for (int i = 0; i < count; i++) {
            position.getLevel().addParticleEffect(
                    position.asVector3f(),
                    particleName,
                    -1,
                    position.getLevel().getDimension(),
                    (Player[]) null
            );
        }
    }
}

package md.maxoneryt.javacombat;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;
import md.maxoneryt.javacombat.combat.CombatManager;
import md.maxoneryt.javacombat.config.ConfigManager;
import md.maxoneryt.javacombat.event.AirSwingEventSubscriber;
import md.maxoneryt.javacombat.event.CombatEventSubscriber;
import md.maxoneryt.javacombat.utils.EntityTargetNotifier;

@Getter
public class JavaCombat extends PluginBase {

    private ConfigManager configManager;
    private CombatManager combatManager;
    private CombatEventSubscriber eventSubscriber;
    private AirSwingEventSubscriber airClickEventSubscriber;
    private EntityTargetNotifier lookAtMobNotifier;

    @Override
    public void onEnable() {
        getLogger().info(TextFormat.GREEN + "JavaCombat enabled");

        this.configManager = new ConfigManager(this);
        configManager.loadConfig();

        this.combatManager = new CombatManager(this);
        this.eventSubscriber = new CombatEventSubscriber(this);
        this.airClickEventSubscriber = new AirSwingEventSubscriber(this);

        this.lookAtMobNotifier = new EntityTargetNotifier(this, combatManager.getDisplayManager());

        eventSubscriber.registerEvents();
        airClickEventSubscriber.registerEvents();
    }

    @Override
    public void onDisable() {
        getLogger().info(TextFormat.RED + "JavaCombat disabled");
        if (combatManager != null) {
            combatManager.cleanup();
        }
        if (lookAtMobNotifier != null) {
            lookAtMobNotifier.cleanup();
        }
    }
}
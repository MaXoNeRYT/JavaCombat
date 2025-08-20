package md.maxoneryt.javacombat.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PluginConfig {
    private int defaultCooldownTicks = 5;
    private double fullDamage = 1.0;
    private double cooldownDamageMultiplier = 0.5;
    private String cooldownTextFormat = "!jc.{0}";
    private double maxCooldownValue = 80.00;
    private boolean cooldownDisplayEnabled = true;

    private boolean sweepAttackEnabled = true;
    private boolean sweepAttackSwordsOnly = true;
    private double sweepDamageMultiplier = 1.0;
    private double sweepAttackRange = 2.5;
    private int sweepParticleCount = 8;

    private Map<String, Integer> itemCooldowns = new HashMap<>();
    private Map<String, Integer> tagCooldowns = new HashMap<>();
    private Map<String, String> itemAnimations = new HashMap<>();
    private Map<String, String> tagAnimations = new HashMap<>();
    private Map<String, String> axeDamageTextures = new HashMap<>();
    private Map<String, Integer> axeDamageValues = new HashMap<>();

    private boolean criticalHitEnabled = true;
    private double criticalHitMultiplier = 1.5;
    private boolean criticalHitRequireAir = true;
    private boolean criticalHitCheckLiquid = true;
    private boolean criticalHitCheckClimbable = true;
    private boolean criticalHitCheckBlocking = true;

    public PluginConfig(int defaultCooldownTicks, double fullDamage, double cooldownDamageMultiplier,
                        String cooldownTextFormat, double maxCooldownValue) {
        this.defaultCooldownTicks = defaultCooldownTicks;
        this.fullDamage = fullDamage;
        this.cooldownDamageMultiplier = cooldownDamageMultiplier;
        this.cooldownTextFormat = cooldownTextFormat;
        this.maxCooldownValue = maxCooldownValue;
    }
}
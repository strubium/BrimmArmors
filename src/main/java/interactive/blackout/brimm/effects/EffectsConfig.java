package interactive.blackout.brimm.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class EffectsConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    /**
     * Config entries:
     * List of strings, format:
     * itemId=effectId:amplifier,effectId:amplifier,...
     *
     * Example:
     * "concord_h=minecraft:regeneration:2,minecraft:fire_resistance:0"
     */
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> HELMET_EFFECTS_CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> CHEST_EFFECTS_CONFIG;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLAST_PROTECTED;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> THORNED;

    public static final Map<String, List<EffectData>> helmetEffects = new HashMap<>();
    public static final Map<String, List<EffectData>> chestEffects = new HashMap<>();
    public static final Set<String> blastProtected = new HashSet<>();
    public static final Set<String> thorned = new HashSet<>();

    static {
        BUILDER.push("Helmet Effects");
        HELMET_EFFECTS_CONFIG = BUILDER.defineList(
                Arrays.asList("helmetEffects"),
                new java.util.function.Supplier<List<? extends String>>() {
                    @Override
                    public List<? extends String> get() {
                        return Arrays.asList(
                                "concord_h=minecraft:regeneration:1",
                                "gpnvg_h=minecraft:night_vision:0",
                                "ghost_h=minecraft:night_vision:0,minecraft:fire_resistance:0",
                                "killa_h=minecraft:fire_resistance:0"
                        );
                    }
                },
                new java.util.function.Predicate<Object>() {
                    @Override
                    public boolean test(Object o) {
                        return o instanceof String;
                    }
                }
        );
        BUILDER.pop();

        BUILDER.push("Chestplate Effects");
        CHEST_EFFECTS_CONFIG = BUILDER.defineList(
                Arrays.asList("chestEffects"),
                () -> Arrays.asList(
                        "marine=minecraft:water_breathing:0",
                        "medic=minecraft:regeneration:1",
                        "defender_iii=minecraft:fire_resistance:0",
                        "concord=minecraft:fire_resistance:0",
                        "horse=minecraft:fire_resistance:0",
                        "spn=minecraft:fire_resistance:0",
                        "atleti=minecraft:movement_speed:1"
                ),
                o -> o instanceof String
        );
        BUILDER.pop();

        BUILDER.push("Enchantments");

        BLAST_PROTECTED = BUILDER.defineListAllowEmpty(
                Arrays.asList("blastProtected"),
                () -> Arrays.asList("saper"),
                o -> o instanceof String
        );

        THORNED = BUILDER.defineListAllowEmpty(
                Arrays.asList("thorned"),
                () -> Arrays.asList("nyyyaaaa"),
                o -> o instanceof String
        );
        BUILDER.pop();

        CONFIG = BUILDER.build();
    }

    /**
     * Call after config is loaded to parse config values into maps & sets
     */
    public static void loadConfig() {
        helmetEffects.clear();
        chestEffects.clear();
        blastProtected.clear();
        thorned.clear();

        parseEffectsMap(HELMET_EFFECTS_CONFIG.get(), helmetEffects);
        parseEffectsMap(CHEST_EFFECTS_CONFIG.get(), chestEffects);

        blastProtected.addAll(BLAST_PROTECTED.get());
        thorned.addAll(THORNED.get());
    }

    /**
     * Parses list of strings with format:
     * itemId=effectId:amplifier,effectId:amplifier,...
     *
     * Puts parsed data into given map
     */
    private static void parseEffectsMap(List<? extends String> entries, Map<String, List<EffectData>> map) {
        if (entries == null) return;

        for (String entry : entries) {
            String[] split = entry.split("=", 2);
            if (split.length != 2) continue;

            String itemId = split[0].trim();
            String effectsString = split[1].trim();

            if (itemId.isEmpty() || effectsString.isEmpty()) continue;

            String[] effectParts = effectsString.split(",");
            List<EffectData> effectDataList = new ArrayList<>();

            for (String effectPart : effectParts) {
                String[] effectSplit = effectPart.split(":");
                if (effectSplit.length < 3) {
                    // ignore invalid
                    continue;
                }
                String effectId = effectSplit[0] + ":" + effectSplit[1];
                int amplifier;
                try {
                    amplifier = Integer.parseInt(effectSplit[2]);
                } catch (NumberFormatException e) {
                    amplifier = 0; // fallback
                }
                effectDataList.add(new EffectData(effectId, amplifier));
            }

            if (!effectDataList.isEmpty()) {
                map.put(itemId, effectDataList);
            }
        }
    }

    public static class EffectData {
        public final String effectId;
        public final int amplifier;

        public EffectData(String effectId, int amplifier) {
            this.effectId = effectId;
            this.amplifier = amplifier;
        }

        public Effect getEffect() {
            return ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectId));
        }

        public EffectInstance toInstance() {
            return new EffectInstance(getEffect(), Integer.MAX_VALUE, amplifier, false, false);
        }
    }
}

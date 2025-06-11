package concord.loottable;

import net.minecraft.loot.*;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static concord.common.items.ItemRegistry.get;

@Mod.EventBusSubscriber
public class LootTableEventHandler {

    // Data class to store item loot info
    private static class LootEntryData {
        final String itemName;
        final int weight;
        final float minCount;
        final float maxCount;

        LootEntryData(String itemName, int weight, float minCount, float maxCount) {
            this.itemName = itemName;
            this.weight = weight;
            this.minCount = minCount;
            this.maxCount = maxCount;
        }
    }

    // Master loot table item mapping
    private static final Map<ResourceLocation, List<LootEntryData>> LOOT_TABLE_ENTRIES = new HashMap<>();

    static {
        // Add entries here
        addEntry(LootTables.ABANDONED_MINESHAFT, new LootEntryData("saper", 17, 1, 2));
        addEntry(LootTables.SIMPLE_DUNGEON,        new LootEntryData("saper", 15, 1, 1));
        addEntry(LootTables.VILLAGE_PLAINS_HOUSE,  new LootEntryData("saper", 7, 1, 1));
        addEntry(LootTables.VILLAGE_SNOWY_HOUSE,   new LootEntryData("saper", 7, 1, 1));
        addEntry(LootTables.VILLAGE_DESERT_HOUSE,  new LootEntryData("saper", 7, 1, 1));
        addEntry(LootTables.VILLAGE_TAIGA_HOUSE,   new LootEntryData("saper", 7, 1, 1));
        addEntry(LootTables.VILLAGE_SAVANNA_HOUSE, new LootEntryData("saper", 7, 1, 1));
    }

    private static void addEntry(ResourceLocation table, LootEntryData entry) {
        LOOT_TABLE_ENTRIES.computeIfAbsent(table, k -> new ArrayList<>()).add(entry);
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        List<LootEntryData> entries = LOOT_TABLE_ENTRIES.get(event.getName());
        if (entries != null) {
            LootPool.Builder poolBuilder = LootPool.lootPool();
            for (LootEntryData entry : entries) {
                poolBuilder.add(ItemLootEntry.lootTableItem(get(entry.itemName))
                        .setWeight(entry.weight)
                        .apply(SetCount.setCount(RandomValueRange.between(entry.minCount, entry.maxCount))));
            }
            event.getTable().addPool(poolBuilder.build());
        }
    }
}

package concord.loottable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static concord.common.items.ItemRegistry.get;

@Mod.EventBusSubscriber
public class LootTableEventHandler {

    // Data class to store item loot info
    public static class LootEntryData {
        final String itemName;
        final int weight;
        final float minCount;
        final float maxCount;

        public LootEntryData(String itemName, int weight, float minCount, float maxCount) {
            this.itemName = itemName;
            this.weight = weight;
            this.minCount = minCount;
            this.maxCount = maxCount;
        }
    }

    // Master loot table item mapping
    private static final Map<ResourceLocation, List<LootEntryData>> LOOT_TABLE_ENTRIES = new HashMap<>();

    static {
        addEntry(new ResourceLocation("minecraft", "chests/abandoned_mineshaft"), new LootEntryData("saper", 17, 1, 2));
        addEntry(new ResourceLocation("minecraft", "chests/simple_dungeon"), new LootEntryData("saper", 15, 1, 1));
        addEntry(new ResourceLocation("minecraft", "chests/village/plains_house"), new LootEntryData("saper", 7, 1, 1));
        addEntry(new ResourceLocation("minecraft", "chests/village/snowy_house"), new LootEntryData("saper", 7, 1, 1));
        addEntry(new ResourceLocation("minecraft", "chests/village/desert_house"), new LootEntryData("saper", 7, 1, 1));
        addEntry(new ResourceLocation("minecraft", "chests/village/taiga_house"), new LootEntryData("saper", 7, 1, 1));
        addEntry(new ResourceLocation("minecraft", "chests/village/savanna_house"), new LootEntryData("saper", 7, 1, 1));
    }

    public static void addEntry(String lootTableName, LootEntryData entry) {
        addEntry(new ResourceLocation(lootTableName), entry);
    }

    private static void addEntry(ResourceLocation table, LootEntryData entry) {
        LOOT_TABLE_ENTRIES.computeIfAbsent(table, k -> new ArrayList<>()).add(entry);
    }

    public static void removeEntry(ResourceLocation table, String itemName) {
        List<LootEntryData> entries = LOOT_TABLE_ENTRIES.get(table);
        if (entries != null) {
            entries.removeIf(entry -> entry.itemName.equals(itemName));
            if (entries.isEmpty()) {
                LOOT_TABLE_ENTRIES.remove(table);
            }
        }
    }

    public static void removeEntry(String lootTableName, String itemName) {
        removeEntry(new ResourceLocation(lootTableName), itemName);
    }


    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        List<LootEntryData> entries = LOOT_TABLE_ENTRIES.get(event.getName());
        if (entries != null) {
            LootPool.Builder poolBuilder = LootPool.lootPool();
            for (LootEntryData entry : entries) {
                poolBuilder.add(LootItem.lootTableItem(get(entry.itemName))
                        .setWeight(entry.weight)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(entry.minCount, entry.maxCount))));
            }
            event.getTable().addPool(poolBuilder.build());
        }
    }

}

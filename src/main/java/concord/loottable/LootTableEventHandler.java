package concord.loottable;

import concord.common.items.ItemRegistry;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static concord.common.items.ItemRegistry.get;

@Mod.EventBusSubscriber()
public class LootTableEventHandler {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation lootTable = event.getName();
        if (lootTable.equals(LootTables.ABANDONED_MINESHAFT)) {
            LootPool pool = LootPool.lootPool()
                    .add(ItemLootEntry.lootTableItem(get("saper"))
                            .setWeight(17)
                            .setQuality(0)
                            .apply(SetCount.setCount(RandomValueRange.between(1.0F, 2.0F))))
                    .build();
            event.getTable().addPool(pool);
        }
        if (lootTable.equals(LootTables.SIMPLE_DUNGEON)) {
            LootPool pool = LootPool.lootPool()
                    .add(ItemLootEntry.lootTableItem(get("saper"))
                            .setWeight(15)
                            .setQuality(0)
                            .apply(SetCount.setCount(RandomValueRange.between(1.0F, 1.0F))))
                    .build();
            event.getTable().addPool(pool);
        }
        if (lootTable.equals(LootTables.VILLAGE_PLAINS_HOUSE)
                || lootTable.equals(LootTables.VILLAGE_SNOWY_HOUSE)
                || lootTable.equals(LootTables.VILLAGE_DESERT_HOUSE)
                || lootTable.equals(LootTables.VILLAGE_TAIGA_HOUSE)
                || lootTable.equals(LootTables.VILLAGE_SAVANNA_HOUSE)) {
            LootPool pool = LootPool.lootPool()
                    .add(ItemLootEntry.lootTableItem(get("saper"))
                            .setWeight(7)
                            .setQuality(0)
                            .apply(SetCount.setCount(RandomValueRange.between(1.0F, 1.0F))))
                    .build();
            event.getTable().addPool(pool);
        }
    }
}

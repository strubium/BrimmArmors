package concord.common.items;

import concord.Concord;
import concord.client.render.Transform;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Concord.MOD_ID);

    public static RegistryObject<Item> getr(String id) {
        return ITEMS.getEntries().stream()
                .filter(entry -> entry.getId().getPath().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No armor found with id: " + id));
    }
    public static Item get(String id) {
        return ITEMS.getEntries().stream()
                .filter(entry -> entry.getId().getPath().equals(id))
                .map(RegistryObject::get)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No armor found with id: " + id));
    }

    public static final RegistryObject<BasicPlate> iron_plate = ITEMS.register("iron_plate", () -> new BasicPlate(ConcordRarity.COMMON));
    public static final RegistryObject<BasicPlate> diamond_plate = ITEMS.register("diamond_plate", () -> new BasicPlate(ConcordRarity.RARE));
    public static final RegistryObject<BasicPlate> nether_plate = ITEMS.register("nether_plate", () -> new BasicPlate(ConcordRarity.EPIC));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}

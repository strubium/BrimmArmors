package concord.common.tile;

import concord.Concord;
import concord.common.blocks.BlockRegistry;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileRegistry {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Concord.MOD_ID);

    public static RegistryObject<TileEntityType<WorkbenchTileEntity>> WORKBENCH_TILE = TILES.register("workbench_tile", () ->
            TileEntityType.Builder.of(WorkbenchTileEntity::new, BlockRegistry.workbench.get(), BlockRegistry.workbench_brf.get(), BlockRegistry.workbench_plate.get(), BlockRegistry.workbench_hlmt.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILES.register(eventBus);
    }

}

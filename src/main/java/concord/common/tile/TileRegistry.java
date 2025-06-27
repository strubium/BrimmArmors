package concord.common.tile;

import concord.Concord;
import concord.common.blocks.BlockRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Concord.MOD_ID);

    public static final RegistryObject<BlockEntityType<WorkbenchTileEntity>> WORKBENCH_TILE = BLOCK_ENTITIES.register("workbench_tile", () ->
            BlockEntityType.Builder.of(WorkbenchTileEntity::new,
                            BlockRegistry.workbench.get(),
                            BlockRegistry.workbench_brf.get(),
                            BlockRegistry.workbench_plate.get(),
                            BlockRegistry.workbench_hlmt.get())
                    .build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

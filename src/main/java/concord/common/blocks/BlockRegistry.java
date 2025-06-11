package concord.common.blocks;

import concord.Concord;
import concord.Resources;
import concord.client.render.Transform;
import concord.common.items.ItemRegistry;
import concord.common.recipes.RecipesManager;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Concord.MOD_ID);

    public static RegistryObject<Block> workbench = register("workbench", () ->
            new WorkbenchBlock("workbench", Resources.WORKBENCH_BASIC_TEXTURE, RecipesManager.CraftType.WORKBENCHES, 0).setTransform(
                    Transform.create().accept(transform -> {
                        transform.WORKBENCH.setTranslate(0, -0.5f, 0).setRotate(0, 0, 0).setScale(20, -20, 20);
                        transform.GUI.setTranslate(0, 0, 0).setRotate(0, 0,0).setScale(0.5f, -0.5f, 0.5f);
                    })
            ), new Item.Properties().tab(Resources.BLOCKS)/*.setISTER(() -> ConcordItemRender::new)*/);

    public static RegistryObject<Block> workbench_plate = register("workbench_plate", () ->
            new WorkbenchBlock("workbench_plate", Resources.WORKBENCH_PLATE_TEXTURE, RecipesManager.CraftType.PLATES, 0).setTransform(
                    Transform.create().accept(transform -> {
                        transform.WORKBENCH.setTranslate(0, -0.5f, 0).setRotate(0, 0, 0).setScale(20, -20, 20);
                        transform.GUI.setTranslate(0, 0, 0).setRotate(0, 0,0).setScale(0.5f, -0.5f, 0.5f);
                    })
            ), new Item.Properties().tab(Resources.BLOCKS)/*.setISTER(() -> ConcordItemRender::new)*/);

    public static RegistryObject<Block> workbench_brf = register("workbench_brf", () ->
            new WorkbenchBlock("workbench_brf", Resources.WORKBENCH_BRF_TEXTURE, RecipesManager.CraftType.BULLETPROOFS, 0).setTransform(
                    Transform.create().accept(transform -> {
                        transform.WORKBENCH.setTranslate(0, -0.5f, 0).setRotate(0, 0, 0).setScale(20, -20, 20);
                        transform.GUI.setTranslate(0, 0, 0).setRotate(0, 0,0).setScale(0.5f, -0.5f, 0.5f);
                    })
            ), new Item.Properties().tab(Resources.BLOCKS)/*.setISTER(() -> ConcordItemRender::new)*/);

    public static RegistryObject<Block> workbench_hlmt = register("workbench_hlmt", () ->
            new WorkbenchBlock("workbench_hlmt", Resources.WORKBENCH_HLMT_TEXTURE, RecipesManager.CraftType.HELMETS, 15).setTransform(
                    Transform.create().accept(transform -> {
                        transform.WORKBENCH.setTranslate(0, -0.8f, 0).setRotate(0, 0, 0).setScale(20, -20, 20);
                        transform.GUI.setTranslate(0, 0, 0).setRotate(0, 0,0).setScale(0.5f, -0.5f, 0.5f);
                    })
            ), new Item.Properties().tab(Resources.BLOCKS)/*.setISTER(() -> ConcordItemRender::new)*/);

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, Item.Properties properties) {
        return register(id, blockSupplier, block1 -> new BlockItem(block1, properties));
    }

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, @Nullable Function<T, BlockItem> supplier) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        if (supplier != null) {
            ItemRegistry.ITEMS.register(id, () -> supplier.apply(registryObject.get()));
        }
        return registryObject;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}

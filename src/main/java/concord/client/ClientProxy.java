package concord.client;

import concord.Concord;
import concord.client.render.WorkbenchRender;
import concord.common.CommonProxy;
import concord.common.blocks.BlockRegistry;
import concord.common.tile.TileRegistry;
import concord.loader.AdvancedModelLoader;
import concord.loader.obj.WavefrontObject;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class ClientProxy extends CommonProxy {

    private static final HashMap<String, WavefrontObject> hashedModels = new HashMap<>();

    public static WavefrontObject getModel(String model) {
        if (hashedModels.containsKey(model))
            return hashedModels.get(model);

        WavefrontObject modelCustom = (WavefrontObject) AdvancedModelLoader.loadModel(
                new ResourceLocation(Concord.MOD_ID, "models/obj/" + model + ".obj"));

        hashedModels.put(model, modelCustom);
        return modelCustom;
    }

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void client() {
        // Block Entity Renderer registration (formerly TileEntityRenderer)
        BlockEntityRenderers.register(TileRegistry.WORKBENCH_TILE.get(), context -> new WorkbenchRender());

        // Render layer setup (formerly RenderTypeLookup)
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.workbench.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.workbench_plate.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.workbench_brf.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.workbench_hlmt.get(), RenderType.cutout());
    }
}

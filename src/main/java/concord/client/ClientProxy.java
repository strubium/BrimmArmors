package concord.client;

import concord.Concord;
import concord.client.render.WorkbenchRender;
import concord.common.CommonProxy;
import concord.common.blocks.BlockRegistry;
import concord.common.tile.TileRegistry;
import concord.loader.AdvancedModelLoader;
import concord.loader.obj.WavefrontObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.HashMap;

public class ClientProxy extends CommonProxy {

    private static final HashMap<String, WavefrontObject> hashedModels = new HashMap<>();

    public static WavefrontObject getModel(String model) {
        if (hashedModels.containsKey(model))
            return hashedModels.get(model);

        WavefrontObject modelCustom = (WavefrontObject) AdvancedModelLoader.loadModel(new ResourceLocation(Concord.MOD_ID, "models/obj/" + model + ".obj"));

        hashedModels.put(model, modelCustom);
        return modelCustom;
    }

    public void preInit() {
        super.preInit();
    }

    public void init() {
        super.init();
    }

    public void client() {
        ClientRegistry.bindTileEntityRenderer(TileRegistry.WORKBENCH_TILE.get(), WorkbenchRender::new);
        RenderTypeLookup.setRenderLayer(BlockRegistry.workbench.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.workbench_plate.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.workbench_brf.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.workbench_hlmt.get(), RenderType.cutout());
    }

}

package interactive.blackout.brimm.client;

import interactive.blackout.brimm.BrimmArmors;
import interactive.blackout.brimm.client.render.WorkbenchRender;
import interactive.blackout.brimm.common.CommonProxy;
import interactive.blackout.brimm.common.blocks.BlockRegistry;
import interactive.blackout.brimm.common.tile.TileRegistry;
import interactive.blackout.brimm.loader.AdvancedModelLoader;
import interactive.blackout.brimm.loader.obj.WavefrontObject;
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

        WavefrontObject modelCustom = (WavefrontObject) AdvancedModelLoader.loadModel(new ResourceLocation(BrimmArmors.MOD_ID, "models/obj/" + model + ".obj"));

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

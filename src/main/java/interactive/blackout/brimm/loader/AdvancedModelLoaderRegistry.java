package interactive.blackout.brimm.loader;

import interactive.blackout.brimm.BrimmArmors;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public enum AdvancedModelLoaderRegistry {

    ARMOR("models/armor");

    private final Map<String, IModelCustom> models = new HashMap<>();
    private final String path;

    AdvancedModelLoaderRegistry(String path) {
        this.path = path;
    }

    public IModelCustom getModel(ResourceLocation location) {
        if (!this.models.containsKey(location.getPath())) {
            IModelCustom model = AdvancedModelLoader.loadModel(location);
            if (model == null) {
                BrimmArmors.LOGGER.error("Could not load model {}, skipping", location);
                return null;
            } else {
                this.models.put(location.getPath(), model);
                return model;
            }
        } else {
            return this.models.get(location.getPath());
        }
    }

    public IModelCustom getModel(String modid, String name) {
        return this.getModel(this.getResource(modid, name));
    }

    private ResourceLocation getResource(String modid, String name) {
        return new ResourceLocation(modid, String.format(this.path + "/%s", name));
    }
}

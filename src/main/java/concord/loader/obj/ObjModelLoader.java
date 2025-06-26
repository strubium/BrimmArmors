package concord.loader.obj;

import concord.loader.IModelCustom;
import concord.loader.IModelCustomLoader;
import concord.loader.ModelFormatException;
import net.minecraft.resources.ResourceLocation;


public class ObjModelLoader implements IModelCustomLoader {

    private static final String[] types = new String[]{"obj"};

    public String getType() {
        return "OBJ model";
    }

    public String[] getSuffixes() {
        return types;
    }

    public IModelCustom loadInstance(ResourceLocation resource) throws ModelFormatException {
        return new WavefrontObject(resource);
    }

}

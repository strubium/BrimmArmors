package interactive.blackout.brimm.loader.obj;

import interactive.blackout.brimm.loader.IModelCustom;
import interactive.blackout.brimm.loader.IModelCustomLoader;
import interactive.blackout.brimm.loader.ModelFormatException;
import net.minecraft.util.ResourceLocation;


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

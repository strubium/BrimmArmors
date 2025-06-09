package concord.loader;

import net.minecraft.util.ResourceLocation;

public interface IModelCustomLoader {

    String getType();

    String[] getSuffixes();

    IModelCustom loadInstance(ResourceLocation var1) throws ModelFormatException;

}

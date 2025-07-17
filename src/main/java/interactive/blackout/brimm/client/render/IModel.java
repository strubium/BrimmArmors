package interactive.blackout.brimm.client.render;

import net.minecraft.util.ResourceLocation;

public interface IModel {

    String getModel();

    ResourceLocation getTexture();

    Transform getTransform();

}

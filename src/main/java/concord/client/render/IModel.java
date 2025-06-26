package concord.client.render;


import net.minecraft.resources.ResourceLocation;

public interface IModel {

    String getModel();

    ResourceLocation getTexture();

    Transform getTransform();

}

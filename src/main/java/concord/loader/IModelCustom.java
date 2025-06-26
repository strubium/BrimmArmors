package concord.loader;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IModelCustom {

    String getType();

    @OnlyIn(Dist.CLIENT)
    void renderAll(PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation texture, int combinedLight, int combinedOverlay);
}

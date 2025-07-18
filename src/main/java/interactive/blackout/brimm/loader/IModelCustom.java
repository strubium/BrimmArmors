package interactive.blackout.brimm.loader;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IModelCustom {

    String getType();

    @OnlyIn(Dist.CLIENT)
    void renderAll(MatrixStack matrix);

    @OnlyIn(Dist.CLIENT)
    void renderAll(ResourceLocation texture, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay);

}

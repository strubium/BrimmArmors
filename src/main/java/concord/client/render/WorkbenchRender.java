package concord.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import concord.client.ClientProxy;
import concord.common.blocks.WorkbenchBlock;
import concord.common.tile.WorkbenchTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class WorkbenchRender implements BlockEntityRenderer<WorkbenchTileEntity> {

    public WorkbenchRender(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WorkbenchTileEntity tile, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockState state = tile.getBlockState();
        Block block = state.getBlock();

        if (block instanceof WorkbenchBlock workbench) {
            Direction facing = state.getValue(WorkbenchBlock.FACING);
            matrix.pushPose();
            matrix.translate(0.5, 0.0, 0.5);
            matrix.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot())); // Negative to match correct rotation

            Minecraft.getInstance().getTextureManager().bindForSetup(workbench.getTexture());
            ClientProxy.getModel(workbench.getModel()).renderAll(workbench.getTexture(), matrix, buffer, combinedLight, combinedOverlay);
            matrix.popPose();
        }
    }
}

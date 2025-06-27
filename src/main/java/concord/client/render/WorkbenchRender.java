package concord.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import concord.client.ClientProxy;
import concord.common.blocks.WorkbenchBlock;
import concord.common.tile.WorkbenchTileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class WorkbenchRender implements BlockEntityRenderer<WorkbenchTileEntity> {

    public WorkbenchRender() {
        super();
    }

    @Override
    public void render(WorkbenchTileEntity tile, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockState state = tile.getBlockState();
        Block block = state.getBlock();

        if (block instanceof WorkbenchBlock workbench) {
            Direction facing = state.getValue(WorkbenchBlock.FACING);
            matrix.pushPose();

            matrix.translate(0.5, 0.0, 0.5);
            matrix.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

            ClientProxy.getModel(workbench.getModel()).renderAll(matrix, buffer, workbench.getTexture(), combinedLight, combinedOverlay);

            matrix.popPose();
        }
    }
}

package interactive.blackout.brimm.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import interactive.blackout.brimm.client.ClientProxy;
import interactive.blackout.brimm.common.blocks.WorkbenchBlock;
import interactive.blackout.brimm.common.tile.WorkbenchTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class WorkbenchRender extends TileEntityRenderer<WorkbenchTileEntity> {

    public WorkbenchRender(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(WorkbenchTileEntity tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        Block block = tile.getBlockState().getBlock();
        if (block instanceof WorkbenchBlock) {
            WorkbenchBlock workbench = (WorkbenchBlock) block;
            Direction value = tile.getBlockState().getValue(WorkbenchBlock.FACING);
            matrix.pushPose();
            matrix.translate(0.5, 0.0, 0.5);
            matrix.mulPose(Vector3f.YP.rotationDegrees(value.toYRot()));
            Minecraft.getInstance().getTextureManager().bind(workbench.getTexture());
            ClientProxy.getModel(workbench.getModel()).renderAll(workbench.getTexture(), matrix, buffer, combinedLight, combinedOverlay);
            matrix.popPose();
        }
    }

}

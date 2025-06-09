package concord.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import concord.client.ClientProxy;
import concord.common.blocks.WorkbenchBlock;
import concord.common.items.BasicArmor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class ConcordItemRender extends ItemStackTileEntityRenderer {

    @Override
    public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType type, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        Item item = itemStack.getItem();
        if (item instanceof IModel) {
            IModel model = (IModel) item;
            matrix.pushPose();
            model.getTransform().apply(type, matrix);
            Minecraft.getInstance().getTextureManager().bind(model.getTexture());
            ClientProxy.getModel(model.getModel()).renderAll(model.getTexture(), matrix, buffer, combinedLight, combinedOverlay);
            matrix.popPose();
        }
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (block instanceof IModel) {
                IModel model = (IModel) block;

                matrix.pushPose();
                model.getTransform().apply(type, matrix);
                Minecraft.getInstance().getTextureManager().bind(model.getTexture());
                ClientProxy.getModel(model.getModel()).renderAll(model.getTexture(), matrix, buffer, combinedLight, combinedOverlay);
                matrix.popPose();
            }
        }
    }

}

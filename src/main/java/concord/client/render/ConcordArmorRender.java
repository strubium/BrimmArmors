package concord.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import concord.client.ClientProxy;
import concord.common.items.BasicArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ConcordArmorRender extends BipedModel<LivingEntity> {

    protected final BasicArmor armor;

    public ConcordArmorRender(BasicArmor armor) {
        super(1F);
        this.armor = armor;
        if (armor.type == EquipmentSlotType.CHEST) {
            this.body = new ModelRenderer(this, 0, 0);
        }
        if (armor.type == EquipmentSlotType.HEAD) {
            this.head = new ModelRenderer(this, 0, 0);
        }
    }



    @Override
    public void renderToBuffer(MatrixStack matrix, IVertexBuilder buffer, int combinedLight, int combinedOverlay, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        matrix.pushPose();
        if (armor.type == EquipmentSlotType.CHEST) {
            this.body.translateAndRotate(matrix);
        }
        if (armor.type == EquipmentSlotType.HEAD) {
            this.head.translateAndRotate(matrix);
        }
        armor.getTransform().ARMOR.setup(matrix);
        Minecraft.getInstance().getTextureManager().bind(armor.getTexture());
        ClientProxy.getModel(armor.getModel()).renderAll(armor.getTexture(), matrix, Minecraft.getInstance().renderBuffers().bufferSource(), combinedLight, combinedOverlay);
        matrix.popPose();
    }

}

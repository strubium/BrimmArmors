package concord.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import concord.client.ClientProxy;
import concord.common.items.BasicArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;

public class ConcordArmorRender extends HumanoidModel<LivingEntity> {

    protected final BasicArmor armor;

    public ConcordArmorRender(ModelPart root, BasicArmor armor) {
        super(root);
        this.armor = armor;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int combinedLight, int combinedOverlay, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();

        // Apply vanilla armor transformations
        if (armor.type == EquipmentSlot.CHEST) {
            this.body.translateAndRotate(poseStack);
        }
        if (armor.type == EquipmentSlot.HEAD) {
            this.head.translateAndRotate(poseStack);
        }

        // Apply any additional transform from the armor item
        armor.getTransform().ARMOR.setup(poseStack);

        // Render the model
        ResourceLocation texture = armor.getTexture();
        ClientProxy.getModel(armor.getModel()).renderAll(texture, poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), combinedLight, combinedOverlay);

        poseStack.popPose();
    }
}

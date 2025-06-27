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
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        poseStack.pushPose();

        if (armor.type == EquipmentSlot.CHEST) {
            this.body.translateAndRotate(poseStack);
        }
        if (armor.type == EquipmentSlot.HEAD) {
            this.head.translateAndRotate(poseStack);
        }

        armor.getTransform().ARMOR.setup(poseStack);

        ResourceLocation texture = armor.getTexture();
        ClientProxy.getModel(armor.getModel())
                .renderAll(poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), texture, packedLight, packedOverlay);

        poseStack.popPose();
    }
}

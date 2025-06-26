package concord.common.items;

import concord.Resources;
import concord.client.render.ConcordArmorRender;
import concord.client.render.IRarity;
import concord.client.render.Transform;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BasicArmor extends ArmorItem implements IRarity {

    public final EquipmentSlot type;
    private final ConcordRarity rarity;
    private final String tooltip;
    private final String model;
    private final ResourceLocation texture;
    private Transform transform;

    public BasicArmor(String model, EquipmentSlot type, ConcordRarity rarity, ConcordArmorMaterial material) {
        super(material, type, new Properties().durability(material.getDurabilityForSlot(type)).tab(Resources.getArmorTab(type)));
        this.type = type;
        this.rarity = rarity;
        this.tooltip = model;

        if (type == EquipmentSlot.CHEST) {
            this.model = "chestplate/" + model;
            this.texture = Resources.path(String.format("textures/chestplate/%s.png", model));
        } else if (type == EquipmentSlot.HEAD) {
            this.model = "helmet/" + model;
            this.texture = Resources.path(String.format("textures/helmet/%s.png", model));
        } else {
            this.model = model;
            this.texture = Resources.path(String.format("textures/%s.png", model));
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal(rarity.color + super.getName(stack).getString());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltipList, TooltipFlag flag) {
        tooltipList.add(Component.literal(rarity.color + "\u00A7o" + I18n.get("tooltip." + tooltip)));
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, A defaultModel) {
        if (slot != this.type) return defaultModel;

        ModelPart modelPart = getModelPart();
        if (modelPart == null) return defaultModel;

        return (A) new ConcordArmorRender(modelPart, this);
    }

    private ModelPart getModelPart() {
        // You will want to create and return your armor model's root ModelPart here.
        // For example:
        // return ClientProxy.getModelPart(model);
        // For now, return null as placeholder:
        return null;
    }

    public Transform getTransform() {
        return transform;
    }

    public BasicArmor setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    public String getModel() {
        return model;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public ConcordRarity getRarity() {
        return rarity;
    }
}

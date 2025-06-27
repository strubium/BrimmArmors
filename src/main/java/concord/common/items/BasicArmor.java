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

    /**
     * Defense and durability must be passed explicitly now, since ArmorMaterial no longer provides them per slot.
     */
    public BasicArmor(String model, EquipmentSlot type, ConcordRarity rarity, ConcordArmorMaterial material,
                      int defense, int durability) {
        // Create ArmorItem.Properties with durability manually set per item
        super(material, equipmentSlotToArmorType(type), new Properties().durability(durability));
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

    public static ArmorItem.Type equipmentSlotToArmorType(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> ArmorItem.Type.HELMET;
            case CHEST -> ArmorItem.Type.CHESTPLATE;
            case LEGS -> ArmorItem.Type.LEGGINGS;
            case FEET -> ArmorItem.Type.BOOTS;
            default -> throw new IllegalArgumentException("Invalid EquipmentSlot: " + slot);
        };
    }

    @Override
    public Component getName(ItemStack stack) {
        // Apply rarity color to the name
        return Component.literal(rarity.color + super.getName(stack).getString());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltipList, TooltipFlag flag) {
        tooltipList.add(Component.literal(rarity.color + "\u00A7o" + I18n.get("tooltip." + tooltip)));
    }

    private ModelPart getModelPart() {
        // TODO: Replace with your actual model part retrieval, e.g.:
        // return ClientProxy.getModelPart(model);
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

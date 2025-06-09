package concord.common.items;

import concord.Resources;
import concord.client.render.IModel;
import concord.client.render.IRarity;
import concord.client.render.Transform;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BasicArmor extends ArmorItem implements IModel, IRarity {

    public EquipmentSlotType type;
    public ConcordRarity rarity;
    private String tooltip;
    private String model;
    private ResourceLocation texture;
    private Transform transform;

    public BasicArmor(String model, EquipmentSlotType type, ConcordRarity rarity, ConcordArmorMaterial material) {
        super(material, type, new Properties().durability(-1).tab(Resources.getArmorTab(type))/*.setISTER(() -> ConcordItemRender::new)*/);
        if (type == EquipmentSlotType.CHEST) {
            this.model = "chestplate/" + model;
            this.texture = Resources.path(String.format("textures/chestplate/%s.png", model));
        }
        if (type == EquipmentSlotType.HEAD) {
            this.model = "helmet/" + model;
            this.texture = Resources.path(String.format("textures/helmet/%s.png", model));
        }
        this.rarity = rarity;
        this.tooltip = model;
        this.type = type;
    }

    @Override
    public ITextComponent getName(ItemStack p_200295_1_) {
        return new StringTextComponent(rarity.color + super.getName(p_200295_1_).getString());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        list.add(new StringTextComponent(rarity.color + TextFormatting.ITALIC.toString() + I18n.get(String.format("tooltip.%s", tooltip))));
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends net.minecraft.client.renderer.entity.model.BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) new concord.client.render.ConcordArmorRender(this);
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    public BasicArmor setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public ConcordRarity getRarity() {
        return rarity;
    }
}

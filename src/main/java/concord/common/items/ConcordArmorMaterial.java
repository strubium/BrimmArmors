package concord.common.items;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class ConcordArmorMaterial implements IArmorMaterial {

    public static final ConcordArmorMaterial common = new ConcordArmorMaterial(
            SoundEvents.ARMOR_EQUIP_GENERIC, "common", 8, 0.0f, 0.0f);

    public static final ConcordArmorMaterial heavy = new ConcordArmorMaterial(
            SoundEvents.ARMOR_EQUIP_GENERIC, "heavy", 9, 5.0f, 1.0f);

    public static final ConcordArmorMaterial hard = new ConcordArmorMaterial(
            SoundEvents.ARMOR_EQUIP_GENERIC, "hard", 10, 10.0f, 1.0f);

    private final int durability;
    private final int defense;
    private final int enchantment;
    private final SoundEvent sound;
    private final Ingredient ingredient;
    private final String name;
    private final float toughness;
    private final float knockbackResistance;

    public ConcordArmorMaterial(SoundEvent sound, String name, int defense, float toughness, float knockbackResistance) {
        this.durability = 0;
        this.enchantment = 0;
        this.ingredient = Ingredient.EMPTY;
        this.sound = sound;
        this.name = name;
        this.defense = defense;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType slot) {
        return durability;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slot) {
        return defense;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantment;
    }

    @Override
    public SoundEvent getEquipSound() {
        return sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return ingredient;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }

}

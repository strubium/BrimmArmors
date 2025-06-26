package concord.common.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import org.jetbrains.annotations.Nullable;

public class ConcordArmorMaterial implements ArmorMaterial {

    public static final ConcordArmorMaterial COMMON = new ConcordArmorMaterial(
            SoundEvents.ARMOR_EQUIP_GENERIC, "common", 8, 0.0f, 0.0f, 240);

    public static final ConcordArmorMaterial HEAVY = new ConcordArmorMaterial(
            SoundEvents.ARMOR_EQUIP_GENERIC, "heavy", 9, 5.0f, 1.0f, 528);

    public static final ConcordArmorMaterial HARD = new ConcordArmorMaterial(
            SoundEvents.ARMOR_EQUIP_GENERIC, "hard", 10, 10.0f, 1.0f, 528);

    private final int durability;
    private final int defense;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final Ingredient repairIngredient;
    private final String name;
    private final float toughness;
    private final float knockbackResistance;

    public ConcordArmorMaterial(SoundEvent equipSound, String name, int defense, float toughness, float knockbackResistance, int durability) {
        this.durability = durability;
        this.enchantmentValue = 0;
        this.repairIngredient = Ingredient.EMPTY;
        this.equipSound = equipSound;
        this.name = name;
        this.defense = defense;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slot) {
        return durability;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slot) {
        return defense;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }

    @Override
    public String getName() {
        // Typically you want to prefix with modid, e.g. "modid:common"
        return "concord:" + name;
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

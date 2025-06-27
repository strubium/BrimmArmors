package concord.common.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class ConcordArmorMaterial implements ArmorMaterial {

    private final String name;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final Ingredient repairIngredient;
    private final float toughness;
    private final float knockbackResistance;

    // Defense and durability arrays indexed by ArmorItem.Type ordinal
    private final int[] defenseValues;
    private final int[] durabilityValues;

    public ConcordArmorMaterial(String name, int enchantmentValue, SoundEvent equipSound,
                                Ingredient repairIngredient, float toughness, float knockbackResistance,
                                int[] defenseValues, int[] durabilityValues) {
        this.name = name;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.repairIngredient = repairIngredient;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.defenseValues = defenseValues;
        this.durabilityValues = durabilityValues;
    }

    @Override
    public String getName() {
        return "concord:" + name;
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
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return defenseValues[type.ordinal()];
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return durabilityValues[type.ordinal()];
    }
}

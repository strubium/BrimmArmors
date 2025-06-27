package concord.resource;

import com.google.gson.*;
import concord.common.items.ConcordArmorMaterial;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;

import java.lang.reflect.Type;
import java.util.Objects;

public class ConcordMaterialSerializer implements JsonDeserializer<ConcordArmorMaterial>, JsonSerializer<ConcordArmorMaterial> {

    @Override
    public ConcordArmorMaterial deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();

        String name = Objects.requireNonNull(obj.get("name"), "name is null!").getAsString();

        int enchantmentValue = obj.has("enchantmentValue") ? obj.get("enchantmentValue").getAsInt() : 0;

        // Deserialize defense array
        JsonArray defenseArray = Objects.requireNonNull(obj.getAsJsonArray("defense"), "defense is null!");
        int[] defenseValues = new int[defenseArray.size()];
        for (int i = 0; i < defenseArray.size(); i++) {
            defenseValues[i] = defenseArray.get(i).getAsInt();
        }

        // Deserialize durability array
        JsonArray durabilityArray = Objects.requireNonNull(obj.getAsJsonArray("durability"), "durability is null!");
        int[] durabilityValues = new int[durabilityArray.size()];
        for (int i = 0; i < durabilityArray.size(); i++) {
            durabilityValues[i] = durabilityArray.get(i).getAsInt();
        }

        float toughness = obj.has("toughness") ? obj.get("toughness").getAsFloat() : 0.0f;
        float knockbackResistance = obj.has("knockbackResistance") ? obj.get("knockbackResistance").getAsFloat() : 0.0f;

        // For simplicity, using ARMOR_EQUIP_GENERIC as equip sound; update as needed
        SoundEvent equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;

        // Repair ingredient handling omitted for now (could add support if needed)
        return new ConcordArmorMaterial(name, enchantmentValue, equipSound, null, toughness, knockbackResistance, defenseValues, durabilityValues);
    }

    @Override
    public JsonElement serialize(ConcordArmorMaterial material, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", material.getName().replace("concord:", "")); // remove modid prefix if you want cleaner JSON
        obj.addProperty("enchantmentValue", material.getEnchantmentValue());
        obj.addProperty("toughness", material.getToughness());
        obj.addProperty("knockbackResistance", material.getKnockbackResistance());

        // Serialize defense array
        JsonArray defenseArray = new JsonArray();
        for (ArmorItem.Type armorType : ArmorItem.Type.values()) {
            defenseArray.add(material.getDefenseForType(armorType));
        }
        obj.add("defense", defenseArray);

        // Serialize durability array
        JsonArray durabilityArray = new JsonArray();
        for (ArmorItem.Type armorType : ArmorItem.Type.values()) {
            durabilityArray.add(material.getDurabilityForType(armorType));
        }
        obj.add("durability", durabilityArray);

        // Could serialize equipSound and repairIngredient if needed

        return obj;
    }
}

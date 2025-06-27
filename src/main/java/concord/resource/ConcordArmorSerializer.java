package concord.resource;

import com.google.gson.*;
import concord.client.render.Transform;
import concord.common.items.BasicArmor;
import concord.common.items.ConcordArmorMaterial;
import concord.common.items.ConcordRarity;
import net.minecraft.world.entity.EquipmentSlot;

import java.lang.reflect.Type;

public class ConcordArmorSerializer implements JsonDeserializer<BasicArmor>, JsonSerializer<BasicArmor> {

    @Override
    public BasicArmor deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();

        String name = obj.get("id").getAsString();
        EquipmentSlot slot = EquipmentSlot.valueOf(obj.get("slot").getAsString().toUpperCase());
        ConcordRarity rarity = ConcordRarity.valueOf(obj.get("rarity").getAsString().toUpperCase());

        Transform transform = context.deserialize(obj.get("transform"), Transform.class);
        ConcordArmorMaterial material = context.deserialize(obj.get("material"), ConcordArmorMaterial.class);

        // Extract defense and durability from material for this slot
        int defense = material.getDefenseForType(BasicArmor.equipmentSlotToArmorType(slot));
        int durability = material.getDurabilityForType(BasicArmor.equipmentSlotToArmorType(slot));

        return new BasicArmor(name, slot, rarity, material, defense, durability).setTransform(transform);
    }

    @Override
    public JsonElement serialize(BasicArmor basicArmor, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        // Assuming BasicArmor stores registry id or has a getter for it:
        obj.addProperty("id", basicArmor.getModel()); // or another getter if you store ID separately

        obj.addProperty("slot", basicArmor.type.getName().toUpperCase());
        obj.addProperty("rarity", basicArmor.getRarity().name().toUpperCase());
        obj.add("material", context.serialize(basicArmor.getMaterial(), ConcordArmorMaterial.class));
        obj.add("transform", context.serialize(basicArmor.getTransform(), Transform.class));

        return obj;
    }
}

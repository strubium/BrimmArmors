package interactive.blackout.brimm.resource;

import com.google.gson.*;
import interactive.blackout.brimm.client.render.Transform;
import interactive.blackout.brimm.common.items.BasicArmor;
import interactive.blackout.brimm.common.items.ConcordArmorMaterial;
import interactive.blackout.brimm.common.items.ConcordRarity;
import net.minecraft.inventory.EquipmentSlotType;

import java.lang.reflect.Type;

public class ConcordArmorSerializer implements JsonDeserializer<BasicArmor>, JsonSerializer<BasicArmor> {
    @Override
    public BasicArmor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        String name = obj.get("id").getAsString();
        EquipmentSlotType slot = EquipmentSlotType.valueOf(obj.get("slot").getAsString().toUpperCase());
        ConcordRarity rarity = ConcordRarity.valueOf(obj.get("rarity").getAsString().toUpperCase());
        Transform transform = jsonDeserializationContext.deserialize(obj.get("transform"), Transform.class);
        return new BasicArmor(name, slot, rarity, jsonDeserializationContext.deserialize(obj.get("material"), ConcordArmorMaterial.class)).setTransform(transform);
    }

    @Override
    public JsonElement serialize(BasicArmor basicArmor, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", basicArmor.getRegistryName().getPath());
        obj.addProperty("slot", basicArmor.type.getName().toUpperCase());
        obj.addProperty("rarity", basicArmor.rarity.name().toUpperCase());
        obj.add("material", jsonSerializationContext.serialize(basicArmor.getMaterial(), ConcordArmorMaterial.class));
        obj.add("transform", jsonSerializationContext.serialize(basicArmor.getTransform(), Transform.class));
        return obj;
    }
}

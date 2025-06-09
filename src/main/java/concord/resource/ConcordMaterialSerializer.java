package concord.resource;

import com.google.gson.*;
import concord.common.items.ConcordArmorMaterial;
import net.minecraft.util.SoundEvents;

import java.lang.reflect.Type;

public class ConcordMaterialSerializer implements JsonDeserializer<ConcordArmorMaterial>, JsonSerializer<ConcordArmorMaterial> {

    public ConcordArmorMaterial deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ConcordArmorMaterial(
                SoundEvents.ARMOR_EQUIP_GENERIC,
                jsonElement.getAsJsonObject().get("name").getAsString(),
                jsonElement.getAsJsonObject().get("defense").getAsInt(),
                jsonElement.getAsJsonObject().get("toughness").getAsFloat(),
                jsonElement.getAsJsonObject().get("knockbackResistance").getAsFloat()
        );
    }

    @Override
    public JsonElement serialize(ConcordArmorMaterial concordArmorMaterial, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", concordArmorMaterial.getName());
        obj.addProperty("defense", concordArmorMaterial.getDefenseForSlot(null)); // null because we don't care about the slot here
        obj.addProperty("toughness", concordArmorMaterial.getToughness());
        obj.addProperty("knockbackResistance", concordArmorMaterial.getKnockbackResistance());
        return obj;
    }
}

package interactive.blackout.brimm.resource;

import com.google.gson.*;
import interactive.blackout.brimm.common.items.ConcordArmorMaterial;
import net.minecraft.util.SoundEvents;

import java.lang.reflect.Type;
import java.util.Objects;

public class ConcordMaterialSerializer implements JsonDeserializer<ConcordArmorMaterial>, JsonSerializer<ConcordArmorMaterial> {

    public ConcordArmorMaterial deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ConcordArmorMaterial(
                SoundEvents.ARMOR_EQUIP_GENERIC,
                jsonElement.getAsJsonObject().get("name").getAsString(),
                jsonElement.getAsJsonObject().get("defense").getAsInt(),
                jsonElement.getAsJsonObject().get("toughness").getAsFloat(),
                jsonElement.getAsJsonObject().get("knockbackResistance").getAsFloat(),
                Objects.requireNonNull(jsonElement.getAsJsonObject().get("durability"), "durability is null!").getAsInt()
        );
    }

    @Override
    public JsonElement serialize(ConcordArmorMaterial concordArmorMaterial, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", concordArmorMaterial.getName());
        obj.addProperty("defense", concordArmorMaterial.getDefenseForSlot(null)); // null because we don't care about the slot here
        obj.addProperty("toughness", concordArmorMaterial.getToughness());
        obj.addProperty("knockbackResistance", concordArmorMaterial.getKnockbackResistance());
        obj.addProperty("durability", concordArmorMaterial.getDurabilityForSlot(null));
        return obj;
    }
}

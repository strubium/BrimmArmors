package concord.resource;

import com.google.gson.*;
import concord.client.render.Transform;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class TransformSerializer implements JsonDeserializer<Transform>, JsonSerializer<Transform> {
    @Override
    public Transform deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray root = jsonElement.getAsJsonArray();
        Transform transform = Transform.create();
        Transform.Matrix matrix = new Transform.Matrix().identify();
        for (JsonElement element : root) {
            JsonObject obj = element.getAsJsonObject();
            String typeName = obj.get("type").getAsString();
            if(obj.has("matrix"))
                matrix = jsonDeserializationContext.deserialize(obj.get("matrix"), Transform.Matrix.class);
            try {
                Field field = Transform.class.getField(typeName);
                field.setAccessible(true);
                field.set(transform, matrix);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new JsonParseException("Unknown transform type: " + typeName, e);
            }
        }
        return transform;
    }

    @Override
    public JsonElement serialize(Transform transform, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray root = new JsonArray();

        //Wierd reflection, but it should work all the time and doesn't need to be performant soo
        for (Field field : transform.getClass().getFields()) {
            Transform.Matrix matrix = new Transform.Matrix();
            try {
                field.setAccessible(true);
                matrix = (Transform.Matrix) field.get(transform);
                JsonObject obj = new JsonObject();
                obj.add("matrix", jsonSerializationContext.serialize(matrix));
            } catch (IllegalAccessException ignored) {}

            JsonObject obj = new JsonObject();
            obj.addProperty("type", field.getName());
            obj.add("matrix", jsonSerializationContext.serialize(matrix));
            if(matrix.equals(Transform.Matrix.IDENTITY)) continue;
            root.add(obj);
        }

        return root;
    }
}

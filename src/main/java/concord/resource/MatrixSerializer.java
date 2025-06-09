package concord.resource;

import com.google.gson.*;
import concord.client.render.Transform;

import java.lang.reflect.Type;

public class MatrixSerializer implements JsonSerializer<Transform.Matrix>, JsonDeserializer<Transform.Matrix> {
    public static JsonObject serialize(Transform.Matrix matrix) {
        JsonObject obj = new JsonObject();
        JsonArray translate = new JsonArray();
            translate.add(matrix.translate[0]);
            translate.add(matrix.translate[1]);
            translate.add(matrix.translate[2]);
        JsonArray rotate = new JsonArray();
            translate.add(matrix.rotate[0]);
            translate.add(matrix.rotate[1]);
            translate.add(matrix.rotate[2]);
        JsonArray scale = new JsonArray();
            translate.add(matrix.scale[0]);
            translate.add(matrix.rotate[1]);
            translate.add(matrix.rotate[2]);

        obj.add("translate", translate);
        obj.add("rotate", rotate);
        obj.add("scale", scale);
        return obj;
    }

    public static Transform.Matrix deserialize(JsonObject obj) {
        Transform.Matrix matrix = new Transform.Matrix();
        if (obj.has("translate")) {
            JsonArray translate = obj.getAsJsonArray("translate");
            matrix.translate[0] = translate.get(0).getAsFloat();
            matrix.translate[1] = translate.get(1).getAsFloat();
            matrix.translate[2] = translate.get(2).getAsFloat();
        }
        if (obj.has("rotate")) {
            JsonArray rotate = obj.getAsJsonArray("rotate");
            matrix.rotate[0] = rotate.get(0).getAsFloat();
            matrix.rotate[1] = rotate.get(1).getAsFloat();
            matrix.rotate[2] = rotate.get(2).getAsFloat();
        }
        if (obj.has("scale")) {
            JsonArray scale = obj.getAsJsonArray("scale");
            matrix.scale[0] = scale.get(0).getAsFloat();
            matrix.scale[1] = scale.get(1).getAsFloat();
            matrix.scale[2] = scale.get(2).getAsFloat();
        }
        return matrix;
    }

    @Override
    public Transform.Matrix deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        Transform.Matrix matrix = new Transform.Matrix().identify();
        if (obj.has("translate")) {
            JsonArray translate = obj.getAsJsonArray("translate");
            matrix.translate[0] = translate.get(0).getAsFloat();
            matrix.translate[1] = translate.get(1).getAsFloat();
            matrix.translate[2] = translate.get(2).getAsFloat();
        }
        if (obj.has("rotate")) {
            JsonArray rotate = obj.getAsJsonArray("rotate");
            matrix.rotate[0] = rotate.get(0).getAsFloat();
            matrix.rotate[1] = rotate.get(1).getAsFloat();
            matrix.rotate[2] = rotate.get(2).getAsFloat();
        }
        if (obj.has("scale")) {
            JsonArray scale = obj.getAsJsonArray("scale");
            matrix.scale[0] = scale.get(0).getAsFloat();
            matrix.scale[1] = scale.get(1).getAsFloat();
            matrix.scale[2] = scale.get(2).getAsFloat();
        }
        return matrix;
    }

    @Override
    public JsonElement serialize(Transform.Matrix matrix, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        JsonArray translate = new JsonArray();
            translate.add(matrix.translate[0]);
            translate.add(matrix.translate[1]);
            translate.add(matrix.translate[2]);
        JsonArray rotate = new JsonArray();
            rotate.add(matrix.rotate[0]);
            rotate.add(matrix.rotate[1]);
            rotate.add(matrix.rotate[2]);
        JsonArray scale = new JsonArray();
            scale.add(matrix.scale[0]);
            scale.add(matrix.scale[1]);
            scale.add(matrix.scale[2]);
        if(matrix.translate[0] + matrix.translate[1] + matrix.translate[2] != 0)
            obj.add("translate", translate);
        if(matrix.rotate[0] + matrix.rotate[1] + matrix.rotate[2] != 0)
            obj.add("rotate", rotate);
        if(matrix.scale[0] + matrix.scale[1] + matrix.scale[2] != 0)
            obj.add("scale", scale);
        return obj;
    }
}

package concord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import concord.client.render.Transform;
import concord.common.items.BasicArmor;
import concord.common.items.ConcordArmorMaterial;
import concord.common.items.ItemRegistry;
import concord.resource.MatrixSerializer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;

@Deprecated
public class ArmorGrabber {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(BasicArmor.class, new concord.resource.ConcordArmorSerializer())
            .registerTypeAdapter(ConcordArmorMaterial.class, new concord.resource.ConcordMaterialSerializer())
            .registerTypeAdapter(Transform.Matrix.class, new concord.resource.MatrixSerializer())
            .registerTypeAdapter(Transform.class, new concord.resource.TransformSerializer())
            .setPrettyPrinting()
            .create();

    public static void init() {
        JsonObject obj = new JsonObject();
        JsonArray arr = new JsonArray();
        for (Item item : ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList())) {
            if (item instanceof concord.common.items.BasicArmor) {
                concord.common.items.BasicArmor armor = (concord.common.items.BasicArmor) item;
                arr.add(GSON.toJson(armor, BasicArmor.class));
            }
        }
        obj.add("armors", arr);
        System.out.println("JSON OBJECT = " + GSON.toJson(obj));
    }
}

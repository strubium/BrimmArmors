package concord.resource;

import com.google.gson.*;
import concord.Concord;
import concord.client.render.Transform;
import concord.common.items.BasicArmor;
import concord.common.items.ConcordArmorMaterial;
import concord.common.items.ItemRegistry;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.function.Supplier;

public final class JsonConfigLoader {
    public static final Supplier<File> CONFIG_PATH = () -> FMLPaths.CONFIGDIR.get().resolve("concord_armors.json").toFile();
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ConcordArmorMaterial.class, new ConcordMaterialSerializer())
            .registerTypeAdapter(BasicArmor.class, new ConcordArmorSerializer())
            .registerTypeAdapter(Transform.Matrix.class, new concord.resource.MatrixSerializer())
            .registerTypeAdapter(Transform.class, new concord.resource.TransformSerializer())
            .setPrettyPrinting()
            .create();

    private static JsonObject createDefaultConfig() throws IOException {
        InputStream stream = Concord.class.getResourceAsStream("/default-config.json");
        String jsRaw = IOUtils.toString(stream);
        return GSON.fromJson(jsRaw, JsonObject.class);
    }

    public static void init() {
        File configFile = CONFIG_PATH.get();
        if(!configFile.exists())
            try {
                Files.createFile(configFile.toPath());
                Files.write(configFile.toPath(), GSON.toJson(createDefaultConfig()).getBytes());

                readConfig(configFile);
            } catch (IOException ignored) {
                Concord.LOGGER.error("Failed to write default config to {}, defaulting values without writing!", configFile.getAbsolutePath());
            }
        else {
            try {
                readConfig(configFile);
            } catch (FileNotFoundException e) {
                Concord.LOGGER.error("Failed to find config {}, this should not be happening! Contact Corrineduck on discord or Jeducklet on CurseForge!", configFile.getAbsolutePath());
            }
        }
    }

    private static void readConfig(File configFile) throws FileNotFoundException {
        JsonObject obj = GSON.fromJson(new FileReader(configFile), JsonObject.class);
        JsonArray armors = obj.getAsJsonArray("armors");
        for (JsonElement armor : armors) {
            Supplier<BasicArmor> basic = ()-> GSON.fromJson(armor, BasicArmor.class);
            ItemRegistry.ITEMS.register(armor.getAsJsonObject().get("id").getAsString(), basic);
        }
    }

}

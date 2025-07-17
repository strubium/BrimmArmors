package concord;

import concord.client.ClientProxy;
import concord.common.CommonProxy;
import concord.common.blocks.BlockRegistry;
import concord.common.items.ItemRegistry;
import concord.common.network.NetworkDispatcher;
import concord.common.tile.TileRegistry;
import concord.effects.EffectsConfig;
import concord.resource.JsonConfigLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Concord.MOD_ID)
public class Concord
{

    public static final String MOD_ID = "concord";
    public static NetworkDispatcher network;
    public static CommonProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Concord() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        JsonConfigLoader.init();

        ItemRegistry.register(eventBus);

        BlockRegistry.register(eventBus);

        TileRegistry.register(eventBus);

        eventBus.addListener(this::preInit);
        eventBus.addListener(this::init);
        eventBus.addListener(this::server);
        eventBus.addListener(this::client);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EffectsConfig.CONFIG);

        EffectsConfig.loadConfig();

    }

    /**
     * @param event Pre Init registry
     */
    private void preInit(final FMLCommonSetupEvent event) {
        network = new NetworkDispatcher();
        network.register();
        proxy.preInit();
    }

    /**
     * @param event EventBus registry
     */
    private void init(FMLLoadCompleteEvent event) {
        proxy.init();
    }

    /**
     * @param event Only for server.bat
     */
    private void server(final FMLDedicatedServerSetupEvent event) {
        proxy.server();
    }

    /**
     * @param event Only for client.bat
     */
    private void client(final FMLClientSetupEvent event) {
        proxy.client();
    }

    static {
         Concord.proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }

}

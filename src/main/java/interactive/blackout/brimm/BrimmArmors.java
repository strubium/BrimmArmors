package interactive.blackout.brimm;

import interactive.blackout.brimm.client.ClientProxy;
import interactive.blackout.brimm.common.CommonProxy;
import interactive.blackout.brimm.common.blocks.BlockRegistry;
import interactive.blackout.brimm.common.items.ItemRegistry;
import interactive.blackout.brimm.common.network.NetworkDispatcher;
import interactive.blackout.brimm.common.tile.TileRegistry;
import interactive.blackout.brimm.effects.EffectsConfig;
import interactive.blackout.brimm.resource.JsonConfigLoader;
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

@Mod(BrimmArmors.MOD_ID)
public class BrimmArmors
{

    public static final String MOD_ID = "brimm_armors";
    public static NetworkDispatcher network;
    public static CommonProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public BrimmArmors() {
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
         BrimmArmors.proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }

}

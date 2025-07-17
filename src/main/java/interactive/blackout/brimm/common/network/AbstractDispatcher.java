package interactive.blackout.brimm.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractDispatcher {

    private final SimpleChannel dispatcher;
    private byte nextPacketID;

    public AbstractDispatcher(String modID) {
        this.dispatcher = NetworkRegistry.newSimpleChannel(new ResourceLocation(modID, "main"), () -> "1", "1"::equals, "1"::equals);
    }

    public SimpleChannel get() {
        return this.dispatcher;
    }

    /**
     * Here you supposed to register packets to handlers
     */
    public abstract void register();

    /**
     * Send message to given player
     */
    public <MSG> void sendTo(MSG message, ServerPlayerEntity player) {
        this.dispatcher.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    /**
     * Send message to all players
     *
     * @param message
     */
    public <MSG> void sendToAll(MSG message) {
        this.dispatcher.send(PacketDistributor.ALL.noArg(), message);
    }

    /**
     * Send message to all players around the given point
     *
     * @param point {@link PacketDistributor.TargetPoint} around which to send
     */
    public <MSG> void sendToAllAround(MSG message, PacketDistributor.TargetPoint point) {
        this.dispatcher.send(PacketDistributor.NEAR.with(() -> point), message);
    }

    /**
     * Send message to the server
     */
    public <MSG> void sendToServer(MSG message) {
        this.dispatcher.send(PacketDistributor.SERVER.noArg(), message);
    }

    /**
     * Register given message with given message handler on a given side
     */
    public <MSG> void register(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        this.dispatcher.registerMessage(nextPacketID++, messageType, encoder, decoder, messageConsumer);
    }

}
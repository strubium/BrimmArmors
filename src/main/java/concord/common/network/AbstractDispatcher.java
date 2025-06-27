package concord.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractDispatcher {

    private final SimpleChannel dispatcher;
    private byte nextPacketID;

    public AbstractDispatcher(String modID) {
        this.dispatcher = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(modID, "main"),
                () -> "1",
                "1"::equals,
                "1"::equals
        );
    }

    public SimpleChannel get() {
        return this.dispatcher;
    }

    /**
     * Here you should register packets to handlers
     */
    public abstract void register();

    /**
     * Send message to a given player
     */
    public <MSG> void sendTo(MSG message, ServerPlayer player) {
        this.dispatcher.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    /**
     * Send message to all players
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
     * Register the given message with the given message handler
     */
    public <MSG> void register(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        this.dispatcher.registerMessage(nextPacketID++, messageType, encoder, decoder, messageConsumer);
    }
}

package concord.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SimplePacket {

    public static void handle(SimplePacket packet, Supplier<NetworkEvent.Context> context) {
        final NetworkEvent.Context ctx = context.get();
        if (ctx.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ctx.enqueueWork(() -> {
                ServerPlayer sender = ctx.getSender();
                packet.server(sender);
            });
            ctx.setPacketHandled(true);
        } else {
            ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> packet.client(clientPlayer())));
            ctx.setPacketHandled(true);
        }
    }

    public void server(ServerPlayer player) {
        // Server-side handler
    }

    @OnlyIn(Dist.CLIENT)
    public void client(LocalPlayer player) {
        // Client-side handler
    }

    @OnlyIn(Dist.CLIENT)
    private static LocalPlayer clientPlayer() {
        return Minecraft.getInstance().player;
    }
}

package interactive.blackout.brimm.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SimplePacket {

    public static void handle(SimplePacket packet, Supplier<NetworkEvent.Context> context) {
        final NetworkEvent.Context ctx = context.get();
        if (ctx.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ctx.enqueueWork(() -> {
                ServerPlayerEntity sender = ctx.getSender();
                packet.server(sender);
            });
            ctx.setPacketHandled(true);
        } else {
            ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> packet.client(clientPlayer())));
            ctx.setPacketHandled(true);
        }
    }

    public void server(ServerPlayerEntity player) {

    }

    @OnlyIn(Dist.CLIENT)
    public void client(ClientPlayerEntity player) {

    }

    @OnlyIn(Dist.CLIENT)
    private static ClientPlayerEntity clientPlayer() {
        return Minecraft.getInstance().player;
    }

}
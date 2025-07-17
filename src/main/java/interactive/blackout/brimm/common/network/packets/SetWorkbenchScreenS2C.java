package interactive.blackout.brimm.common.network.packets;

import interactive.blackout.brimm.client.screens.WorkbenchScreen;
import interactive.blackout.brimm.common.network.SimplePacket;
import interactive.blackout.brimm.common.recipes.RecipesManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SetWorkbenchScreenS2C extends SimplePacket {
    RecipesManager.CraftType craftType;

    public SetWorkbenchScreenS2C(RecipesManager.CraftType type) {
        this.craftType = type;
    }

    public static void read(SetWorkbenchScreenS2C msg, PacketBuffer packetBuffer) {
        packetBuffer.writeEnum(msg.craftType);
    }

    public static SetWorkbenchScreenS2C write(PacketBuffer packetBuffer) {
        return new SetWorkbenchScreenS2C(packetBuffer.readEnum(RecipesManager.CraftType.class));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void client(ClientPlayerEntity player) {
        Minecraft.getInstance().setScreen(new WorkbenchScreen(craftType));
    }
}

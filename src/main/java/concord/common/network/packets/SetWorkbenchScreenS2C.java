package concord.common.network.packets;

import concord.client.screens.WorkbenchScreen;
import concord.common.network.SimplePacket;
import concord.common.recipes.RecipesManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SetWorkbenchScreenS2C extends SimplePacket {
    RecipesManager.CraftType craftType;

    public SetWorkbenchScreenS2C(RecipesManager.CraftType type) {
        this.craftType = type;
    }

    // Encoder (write to buffer)
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(craftType);
    }

    // Decoder (read from buffer)
    public static SetWorkbenchScreenS2C decode(FriendlyByteBuf buffer) {
        return new SetWorkbenchScreenS2C(buffer.readEnum(RecipesManager.CraftType.class));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void client(LocalPlayer player) {
        Minecraft.getInstance().setScreen(new WorkbenchScreen(craftType));
    }
}

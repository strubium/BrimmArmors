package concord.common.items;

import concord.client.render.IRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BasicPlate extends Item implements IRarity {

    private final ConcordRarity rarity;

    public BasicPlate(ConcordRarity rarity) {
        super(new Properties());
        this.rarity = rarity;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal(rarity.color + super.getName(stack).getString());
    }

    @Override
    public ConcordRarity getRarity() {
        return rarity;
    }
}

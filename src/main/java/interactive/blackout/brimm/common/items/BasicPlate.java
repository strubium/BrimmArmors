package interactive.blackout.brimm.common.items;

import interactive.blackout.brimm.Resources;
import interactive.blackout.brimm.client.render.IRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class BasicPlate extends Item implements IRarity {

    private final ConcordRarity rarity;

    public BasicPlate(ConcordRarity rarity) {
        super(new Properties().tab(Resources.ITEMS));
        this.rarity = rarity;
    }

    @Override
    public ITextComponent getName(ItemStack p_200295_1_) {
        return new StringTextComponent(rarity.color + super.getName(p_200295_1_).getString());
    }

    @Override
    public ConcordRarity getRarity() {
        return rarity;
    }
}

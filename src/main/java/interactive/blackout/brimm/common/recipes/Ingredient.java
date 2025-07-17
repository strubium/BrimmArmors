package interactive.blackout.brimm.common.recipes;

import net.minecraft.item.Item;

public class Ingredient {

    public final Item item;
    public final int count;

    public Ingredient(Item item, int count) {
        this.item = item;
        this.count = count;
    }

}

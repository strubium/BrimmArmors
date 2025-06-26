package concord.common.recipes;


import net.minecraft.world.item.Item;

public class Ingredient {

    public final Item item;
    public final int count;

    public Ingredient(Item item, int count) {
        this.item = item;
        this.count = count;
    }

}

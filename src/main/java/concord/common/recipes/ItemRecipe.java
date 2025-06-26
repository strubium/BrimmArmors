package concord.common.recipes;


import net.minecraft.world.item.Item;

public class ItemRecipe {

    public final Item result;
    public final Ingredient[] ingredients;

    public ItemRecipe(Item result, Ingredient... ingredients) {
        this.result = result;
        this.ingredients = ingredients;
    }

}

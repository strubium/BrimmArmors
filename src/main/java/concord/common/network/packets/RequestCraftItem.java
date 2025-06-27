package concord.common.network.packets;

import concord.Concord;
import concord.common.network.SimplePacket;
import concord.common.recipes.Ingredient;
import concord.common.recipes.ItemRecipe;
import concord.common.recipes.RecipesManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class RequestCraftItem extends SimplePacket {

    public RecipesManager.CraftType craftType;
    public short currentIndex;

    public RequestCraftItem() {
    }

    public RequestCraftItem(RecipesManager.CraftType craftType, short currentIndex) {
        this.craftType = craftType;
        this.currentIndex = currentIndex;
    }

    public static void read(RequestCraftItem msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.craftType.getRecipeID());
        buffer.writeShort(msg.currentIndex);
    }

    public static RequestCraftItem write(FriendlyByteBuf buffer) {
        return new RequestCraftItem(RecipesManager.CraftType.get(buffer.readUtf()), buffer.readShort());
    }

    @Override
    public void server(ServerPlayer player) {
        ArrayList<ItemRecipe> recipes = RecipesManager.getRecipe(craftType);
        if (recipes == null || currentIndex < 0 || currentIndex >= recipes.size()) {
            Concord.LOGGER.error("Someone is trying to craft something with a weird currentIndex!");
            return;  // Invalid request
        }

        try {
            Inventory inventory = player.getInventory();
            ItemRecipe itemRecipe = recipes.get(currentIndex);

            boolean hasIngredients = true;

            for (Ingredient ingredient : itemRecipe.ingredients) {
                if (ingredient.count > inventory.countItem(ingredient.item)) {
                    hasIngredients = false;
                    break;
                }
            }

            if (hasIngredients) {
                for (Ingredient ingredient : itemRecipe.ingredients) {
                    int countToRemove = ingredient.count;
                    for (int i = 0; i < inventory.getContainerSize(); i++) {
                        ItemStack stack = inventory.getItem(i);
                        if (!stack.isEmpty() && stack.getItem() == ingredient.item) {
                            int stackCount = stack.getCount();
                            if (stackCount <= countToRemove) {
                                inventory.removeItem(i, stackCount);
                                countToRemove -= stackCount;
                            } else {
                                inventory.removeItem(i, countToRemove);
                                countToRemove = 0;
                            }
                            if (countToRemove <= 0) {
                                break;
                            }
                        }
                    }
                }

                ItemStack resultItem = new ItemStack(itemRecipe.result);
                if (!inventory.add(resultItem)) {
                    player.drop(resultItem, false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

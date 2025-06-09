package concord.common.network.packets;

import concord.common.network.SimplePacket;
import concord.common.recipes.Ingredient;
import concord.common.recipes.ItemRecipe;
import concord.common.recipes.RecipesManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;

public class RequestCraftItem extends SimplePacket {

    public RecipesManager.CraftType craftType;
    public int currentIndex;

    public RequestCraftItem() {

    }

    public RequestCraftItem(RecipesManager.CraftType craftType, int currentIndex) {
        this.craftType = craftType;
        this.currentIndex = currentIndex;
    }

    public static void read(RequestCraftItem msg, PacketBuffer packetBuffer) {
        packetBuffer.writeUtf(msg.craftType.getRecipeID());
        packetBuffer.writeInt(msg.currentIndex);
    }

    public static RequestCraftItem write(PacketBuffer packetBuffer) {
        return new RequestCraftItem(RecipesManager.CraftType.get(packetBuffer.readUtf()), packetBuffer.readInt());
    }


    @Override
    public void server(ServerPlayerEntity player) {
        ArrayList<ItemRecipe> recipe = RecipesManager.getRecipe(craftType);
        if (recipe != null) {
            try {
                PlayerInventory inventory = player.inventory;
                ItemRecipe itemRecipe = recipe.get(currentIndex);

                boolean flag = true;

                for (Ingredient ingredient : itemRecipe.ingredients) {
                    if (ingredient.count > inventory.countItem(ingredient.item)) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
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

}

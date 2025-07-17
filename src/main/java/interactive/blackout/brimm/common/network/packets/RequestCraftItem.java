package interactive.blackout.brimm.common.network.packets;

import interactive.blackout.brimm.BrimmArmors;
import interactive.blackout.brimm.common.network.SimplePacket;
import interactive.blackout.brimm.common.recipes.Ingredient;
import interactive.blackout.brimm.common.recipes.ItemRecipe;
import interactive.blackout.brimm.common.recipes.RecipesManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

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

    public static void read(RequestCraftItem msg, PacketBuffer packetBuffer) {
        packetBuffer.writeUtf(msg.craftType.getRecipeID());
        packetBuffer.writeShort(msg.currentIndex);
    }

    public static RequestCraftItem write(PacketBuffer packetBuffer) {
        return new RequestCraftItem(RecipesManager.CraftType.get(packetBuffer.readUtf()), packetBuffer.readShort());
    }


    @Override
    public void server(ServerPlayerEntity player) {
        ArrayList<ItemRecipe> recipes = RecipesManager.getRecipe(craftType);
        if (recipes == null || currentIndex < 0 || currentIndex >= recipes.size()) {
            BrimmArmors.LOGGER.error("Someone is trying to craft something with a weird currentIndex!");
            return;  // invalid request
        }

        try {
            PlayerInventory inventory = player.inventory;
            ItemRecipe itemRecipe = recipes.get(currentIndex);

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

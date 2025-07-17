package interactive.blackout.brimm.common.recipes;

import interactive.blackout.brimm.common.blocks.BlockRegistry;
import interactive.blackout.brimm.common.items.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;

import static interactive.blackout.brimm.common.items.ItemRegistry.get;

public class RecipesManager {

    private static final ArrayList<ItemRecipe> workbenches = new ArrayList<>();

    private static final ArrayList<ItemRecipe> plates = new ArrayList<>();

    private static final ArrayList<ItemRecipe> helmets = new ArrayList<>();

    private static final ArrayList<ItemRecipe> bulletproofs = new ArrayList<>();

    public static void init() {
        addWorkbenchRecipe(CraftType.WORKBENCHES, r(BlockRegistry.workbench_plate.get().asItem(), i(Items.IRON_INGOT, 60), i(Items.OAK_PLANKS, 30)));
        addWorkbenchRecipe(CraftType.WORKBENCHES, r(BlockRegistry.workbench_brf.get().asItem(), i(Items.IRON_INGOT, 40), i(Items.OAK_PLANKS, 20), i(Items.LEATHER, 20), i(Items.WHITE_WOOL, 10)));
        addWorkbenchRecipe(CraftType.WORKBENCHES, r(BlockRegistry.workbench_hlmt.get().asItem(), i(Items.IRON_INGOT, 30), i(Items.OAK_PLANKS, 25), i(Items.LEATHER, 30), i(Items.WHITE_WOOL, 5)));

        addWorkbenchRecipe(CraftType.PLATES, r(ItemRegistry.IRON_PLATE.get(), i(Items.IRON_INGOT, 5)));
        addWorkbenchRecipe(CraftType.PLATES, r(ItemRegistry.DIAMOND_PLATE.get(), i(Items.IRON_INGOT, 10), i(Items.DIAMOND, 5)));
        addWorkbenchRecipe(CraftType.PLATES, r(ItemRegistry.NETHER_PLATE.get(), i(Items.IRON_INGOT, 10), i(Items.DIAMOND, 5), i(Items.NETHERITE_INGOT, 1)));

        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("nato"), i(ItemRegistry.IRON_PLATE.get(), 2), i(Items.LEATHER, 25)));
        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("nato_ii"), i(get("nato"), 1), i(ItemRegistry.DIAMOND_PLATE.get(), 2), i(Items.LEATHER, 20)));
        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("marine"), i(get("nato"), 1), i(ItemRegistry.NETHER_PLATE.get(), 2), i(Items.LEATHER, 45), i(Items.SALMON, 5), i(Items.HEART_OF_THE_SEA, 1)));

        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("guard"), i(get("nato_ii"), 1), i(ItemRegistry.NETHER_PLATE.get(), 2), i(Items.LEATHER, 45)));
        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("vanderer"), i(get("nato_ii"), 1), i(ItemRegistry.NETHER_PLATE.get(), 2), i(Items.LEATHER, 30), i(Items.CACTUS, 5)));

        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("defender"), i(ItemRegistry.IRON_PLATE.get(), 4), i(Items.LEATHER, 20)));
        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("defender_ii"), i(get("defender"), 1), i(ItemRegistry.DIAMOND_PLATE.get(), 6), i(Items.LEATHER, 35)));
        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("defender_iii"), i(get("defender_ii"), 1), i(ItemRegistry.NETHER_PLATE.get(), 4), i(Items.LEATHER, 45)));

        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("concord"), i(ItemRegistry.NETHER_PLATE.get(), 4), i(Items.NETHER_STAR, 1), i(Items.BLAZE_ROD, 8), i(Items.LEATHER, 40)));

        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("pmc"), i(ItemRegistry.IRON_PLATE.get(), 2), i(Items.LEATHER, 20)));
        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("assault"), i(get("pmc"), 1), i(ItemRegistry.DIAMOND_PLATE.get(), 4), i(Items.LEATHER, 45)));

        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("horse"), i(get("assault"), 1), i(ItemRegistry.NETHER_PLATE.get(), 2), i(Items.LEATHER, 35)));
        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("spn"), i(get("assault"), 1), i(ItemRegistry.NETHER_PLATE.get(), 2), i(Items.LEATHER, 35)));

        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("atleti"), i(get("spn"), 1), i(ItemRegistry.NETHER_PLATE.get(), 2), i(Items.DIAMOND, 3), i(Items.LEATHER, 20), i(Items.SUGAR, 10), i(Items.GLOWSTONE_DUST, 5)));

        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("ratnik"), i(ItemRegistry.IRON_PLATE.get(), 2), i(ItemRegistry.DIAMOND_PLATE.get(), 2), i(Items.LEATHER, 15)));
        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("ratnik_advance"), i(get("ratnik"), 1), i(Items.LEATHER, 15)));
        addWorkbenchRecipe(CraftType.BULLETPROOFS, r(get("veteran"), i(get("ratnik_advance"), 1), i(ItemRegistry.DIAMOND_PLATE.get(), 4), i(Items.LEATHER, 35)));

        addWorkbenchRecipe(CraftType.HELMETS, r(get("base_h"), i(Items.IRON_INGOT, 20), i(Items.LEATHER, 10)));
        addWorkbenchRecipe(CraftType.HELMETS, r(get("assault_h"), i(get("base_h"), 1), i(Items.IRON_INGOT, 30), i(Items.REDSTONE, 10), i(Items.LAPIS_LAZULI, 10)));
        addWorkbenchRecipe(CraftType.HELMETS, r(get("gasmask_h"), i(get("base_h"), 1), i(Items.IRON_INGOT, 45), i(Items.COAL, 30), i(Items.LEATHER, 10), i(Items.GLASS, 15)));

        addWorkbenchRecipe(CraftType.HELMETS, r(get("medic_h"), i(get("assault_h"), 1), i(Items.DIAMOND, 10), i(Items.IRON_INGOT, 15), i(Items.LEATHER, 10), i(Items.ENCHANTED_GOLDEN_APPLE, 1)));
        addWorkbenchRecipe(CraftType.HELMETS, r(get("concord_h"), i(get("assault_h"), 1), i(Items.NETHERITE_INGOT, 2), i(Items.IRON_INGOT, 25), i(Items.LEATHER, 10), i(Items.BLAZE_ROD, 1)));

        addWorkbenchRecipe(CraftType.HELMETS, r(get("infantry_h"), i(Items.IRON_INGOT, 20), i(Items.LEATHER, 10)));
        addWorkbenchRecipe(CraftType.HELMETS, r(get("gpnvg_h"), i(get("infantry_h"), 1), i(Items.DIAMOND, 10), i(Items.IRON_INGOT, 15), i(Items.LEATHER, 10), i(Items.SPIDER_EYE, 15)));
        addWorkbenchRecipe(CraftType.HELMETS, r(get("ghost_h"), i(get("gpnvg_h"), 1), i(Items.NETHERITE_INGOT, 2), i(Items.IRON_INGOT, 25), i(Items.LEATHER, 10), i(Items.SKELETON_SKULL, 1)));

        addWorkbenchRecipe(CraftType.HELMETS, r(get("zch_h"), i(Items.IRON_INGOT, 20), i(Items.LEATHER, 5)));
        addWorkbenchRecipe(CraftType.HELMETS, r(get("zabralo_h"), i(get("zch_h"), 1), i(Items.DIAMOND, 10), i(Items.IRON_INGOT, 30), i(Items.GLASS, 64), i(Items.LEATHER, 10)));
        addWorkbenchRecipe(CraftType.HELMETS, r(get("killa_h"), i(get("zabralo_h"), 1), i(Items.NETHERITE_INGOT, 2), i(Items.IRON_INGOT, 30), i(Items.LEATHER, 10), i(Items.BLACK_DYE, 5), i(Items.WHITE_DYE, 2)));

        addWorkbenchRecipe(CraftType.HELMETS, r(get("mk_ii_h"), i(Items.IRON_INGOT, 30), i(Items.LEATHER, 5)));
        addWorkbenchRecipe(CraftType.HELMETS, r(get("saper_h"), i(get("mk_ii_h"), 1), i(Items.DIAMOND, 5), i(Items.IRON_INGOT, 15), i(Items.LEATHER, 5)));
    }

    public static ItemRecipe r(Item item, Ingredient... ingredients) {
        return new ItemRecipe(item, ingredients);
    }
    
    public static Ingredient i(Item item, int count) {
        return new Ingredient(item, count);
    }
    
    public static ArrayList<ItemRecipe> getRecipe(CraftType recipeID) {
        switch (recipeID) {
            case WORKBENCHES:
                return workbenches;
            case PLATES:
                return plates;
            case HELMETS:
                return helmets;
            case BULLETPROOFS:
                return bulletproofs;
            default:
                return null;
        }
    }

    public static void addWorkbenchRecipe(CraftType type, ItemRecipe recipe) {
        switch (type) { //TODO Smeagle: When we remove the other benches we will need to replace this
            case WORKBENCHES:
                workbenches.add(recipe);
                break;
            case PLATES:
                plates.add(recipe);
                break;
            case HELMETS:
                helmets.add(recipe);
                break;
            case BULLETPROOFS:
                bulletproofs.add(recipe);
                break;
            default:
                throw new IllegalArgumentException("Unknown CraftType: " + type);
        }
    }

    public enum CraftType {
        WORKBENCHES("workbenches"), PLATES("plates"), HELMETS("helmets"), BULLETPROOFS("bulletproofs");

        private final String recipeID;

        CraftType(String recipeID) {
            this.recipeID = recipeID;
        }

        public static CraftType get(String recipeID) {
            for (CraftType value : values()) {
                if (value.getRecipeID().equals(recipeID)) {
                    return value;
                }
            }
            return null;
        }

        public String getRecipeID() {
            return recipeID;
        }

    }

}

package concord;

import concord.common.blocks.BlockRegistry;
import concord.common.items.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import static concord.common.items.ItemRegistry.get;

public class Resources {

    public static final ResourceLocation WORKBENCH_BASIC_TEXTURE = path("textures/workbench/workbench.png");
    public static final ResourceLocation WORKBENCH_PLATE_TEXTURE = path("textures/workbench/workbench_plate.png");
    public static final ResourceLocation WORKBENCH_BRF_TEXTURE = path("textures/workbench/workbench_brf.png");
    public static final ResourceLocation WORKBENCH_HLMT_TEXTURE = path("textures/workbench/workbench_hlmt.png");

    public static final ItemGroup HELMET = new ItemGroup("helmet") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(get("gpnvg_h"));
        }
    };

    public static final ItemGroup BULLETPROOF = new ItemGroup("bulletproof") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(get("concord"));
        }
    };

    public static final ItemGroup BLOCKS = new ItemGroup("blocks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockRegistry.workbench.get());
        }
    };

    public static final ItemGroup ITEMS = new ItemGroup("items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.IRON_PLATE.get());
        }
    };

    public static ResourceLocation path(String path) {
        return new ResourceLocation(Concord.MOD_ID, path);
    }

    public static ItemGroup getArmorTab(EquipmentSlot type) {
        if (type == EquipmentSlot.CHEST) {
            return BULLETPROOF;
        }
        if (type == EquipmentSlot.HEAD) {
            return HELMET;
        }
        return null;
    }
}

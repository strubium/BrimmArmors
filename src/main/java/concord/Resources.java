package concord;

import concord.common.blocks.BlockRegistry;
import concord.common.items.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static concord.common.items.ItemRegistry.get;

public class Resources {

    public static final ResourceLocation WORKBENCH_BASIC_TEXTURE = path("textures/workbench/workbench.png");
    public static final ResourceLocation WORKBENCH_PLATE_TEXTURE = path("textures/workbench/workbench_plate.png");
    public static final ResourceLocation WORKBENCH_BRF_TEXTURE = path("textures/workbench/workbench_brf.png");
    public static final ResourceLocation WORKBENCH_HLMT_TEXTURE = path("textures/workbench/workbench_hlmt.png");

    public static final CreativeModeTab HELMET = CreativeModeTab.builder()
            .title(Component.literal("concord.helmet"))
            .icon(() -> new ItemStack(get("gpnvg_h")))
            .build();

    public static final CreativeModeTab BULLETPROOF = CreativeModeTab.builder()
            .title(Component.literal("concord.bulletproof"))
            .icon(() -> new ItemStack(get("concord")))
            .build();

    public static final CreativeModeTab BLOCKS = CreativeModeTab.builder()
            .title(Component.literal("concord.blocks"))
            .icon(() -> new ItemStack(BlockRegistry.workbench.get()))
            .build();

    public static final CreativeModeTab ITEMS = CreativeModeTab.builder()
            .title(Component.literal("concord.items"))
            .icon(() -> new ItemStack(ItemRegistry.IRON_PLATE.get()))
            .build();

    public static ResourceLocation path(String path) {
        return new ResourceLocation(Concord.MOD_ID, path);
    }

    public static CreativeModeTab getArmorTab(EquipmentSlot type) {
        if (type == EquipmentSlot.CHEST) {
            return BULLETPROOF;
        }
        if (type == EquipmentSlot.HEAD) {
            return HELMET;
        }
        return null;
    }
}

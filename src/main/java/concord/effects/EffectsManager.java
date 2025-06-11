package concord.effects;

import concord.common.items.BasicArmor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static concord.common.items.ItemRegistry.get;

@Mod.EventBusSubscriber
public class EffectsManager {

    @SubscribeEvent
    public static void onEquipment(LivingEquipmentChangeEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            ItemStack to = event.getTo();
            ItemStack from = event.getFrom();
            if (event.getSlot() == EquipmentSlotType.HEAD) {
                onAddHelmet(player, to);
            }
            if (event.getSlot() == EquipmentSlotType.CHEST) {
                onAddChest(player, to);
            }
            onRemove(player, from);
        }
    }

    public static void onAddHelmet(PlayerEntity player, ItemStack to) {
        if (to.getItem() == get("concord_h")) {
            player.addEffect(new EffectInstance(Effects.REGENERATION, Integer.MAX_VALUE, 2, false, false));
            player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("gpnvg_h")) {
            player.addEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("ghost_h")) {
            player.addEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
            player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("killa_h")) {
            player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    public static void onAddChest(PlayerEntity player, ItemStack to) {
        if (to.getItem() == get("marine")) {
            player.addEffect(new EffectInstance(Effects.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
            player.addEffect(new EffectInstance(Effects.DOLPHINS_GRACE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("saper")) {
            enchant(to, Enchantments.BLAST_PROTECTION, 10);
        }
        if (to.getItem() == get("medic")) {
            player.addEffect(new EffectInstance(Effects.REGENERATION, Integer.MAX_VALUE, 3, false, false));
        }
        if (to.getItem() == get("defender_iii")) {
            player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("concord")) {
            player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("horse")) {
            player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("spn")) {
            player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("atleti")) {
            player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE, 3, false, false));
            player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("nyyyaaaa")) {
            enchant(to, Enchantments.THORNS, 10);
        }
    }

    public static void onRemove(PlayerEntity player, ItemStack from) {
        if (from.getItem() == get("marine")) {
            player.removeEffect(Effects.WATER_BREATHING);
            player.removeEffect(Effects.DOLPHINS_GRACE);
        }
        if (from.getItem() == get("medic")) {
            player.removeEffect(Effects.REGENERATION);
        }
        if (from.getItem() == get("defender_iii")) {
            player.removeEffect(Effects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("concord")) {
            player.removeEffect(Effects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("horse")) {
            player.removeEffect(Effects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("spn")) {
            player.removeEffect(Effects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("atleti")) {
            player.removeEffect(Effects.MOVEMENT_SPEED);
            player.removeEffect(Effects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("concord_h")) {
            player.removeEffect(Effects.REGENERATION);
            player.removeEffect(Effects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("gpnvg_h")) {
            player.removeEffect(Effects.NIGHT_VISION);
        }
        if (from.getItem() == get("ghost_h")) {
            player.removeEffect(Effects.NIGHT_VISION);
            player.removeEffect(Effects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("killa_h")) {
            player.removeEffect(Effects.FIRE_RESISTANCE);
        }
    }

    public static void enchant(ItemStack itemStack, Enchantment enchantment, int level) {
        boolean flag = true;
        for (INBT enchantmentTag : itemStack.getEnchantmentTags()) {
            String asString = enchantmentTag.getAsString();
            if (asString.contains(enchantment.getRegistryName().toString())) {
                flag = false;
            }
        }
        if (flag) {
            itemStack.enchant(enchantment, level);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        PlayerInventory inventory = player.inventory;
        ItemStack helmet = inventory.armor.get(3);
        ItemStack chest = inventory.armor.get(2);
        ItemStack carried = inventory.getCarried();
        if (carried.getItem() instanceof BasicArmor) {
            if (!carried.getEnchantmentTags().isEmpty()) {
                carried.getEnchantmentTags().clear();
            }
        }
        for (ItemStack item : player.inventory.items) {
            if (item.getItem() instanceof BasicArmor) {
                if (!item.getEnchantmentTags().isEmpty()) {
                    item.getEnchantmentTags().clear();
                }
            }
        }
        if (helmet.getItem() == get("gasmask_h")) {
            player.removeEffect(Effects.POISON);
            player.removeEffect(Effects.BLINDNESS);
            player.removeEffect(Effects.HUNGER);
            player.removeEffect(Effects.CONFUSION);
            player.removeEffect(Effects.WITHER);
            player.removeEffect(Effects.MOVEMENT_SLOWDOWN);
            player.removeEffect(Effects.WEAKNESS);
        }
        if (event.side.isServer()) {
            if (helmet.getItem() == get("medic_h")) {
                List<Entity> entities = player.level.getEntities(player, player.getBoundingBox().expandTowards(-5, -5, -5).expandTowards(5, 5, 5));
                boolean flag = chest.getItem() == get("medic");
                for (Entity entity : entities) {
                    if (entity instanceof PlayerEntity) {
                        if (!((PlayerEntity) entity).hasEffect(Effects.REGENERATION)) {
                            ((PlayerEntity) entity).addEffect(new EffectInstance(Effects.REGENERATION, 100, flag ? 4 : 3, false, false));
                        }
                    }
                }
                if (!player.hasEffect(Effects.REGENERATION)) {
                    player.addEffect(new EffectInstance(Effects.REGENERATION, 100, flag ? 4 : 3, false, false));
                }
            }
        }
    }

}

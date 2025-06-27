package concord.effects;

import concord.common.items.BasicArmor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

import static concord.common.items.ItemRegistry.get;

@Mod.EventBusSubscriber
public class EffectsManager {

    @SubscribeEvent
    public static void onEquipment(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack to = event.getTo();
            ItemStack from = event.getFrom();
            if (event.getSlot() == EquipmentSlot.HEAD) {
                onAddHelmet(player, to);
            }
            if (event.getSlot() == EquipmentSlot.CHEST) {
                onAddChest(player, to);
            }
            onRemove(player, from);
        }
    }

    public static void onAddHelmet(Player player, ItemStack to) {
        if (to.getItem() == get("concord_h")) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, Integer.MAX_VALUE, 2, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("gpnvg_h")) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("ghost_h")) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("killa_h")) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    public static void onAddChest(Player player, ItemStack to) {
        if (to.getItem() == get("marine")) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("saper")) {
            enchant(to, net.minecraft.world.item.enchantment.Enchantments.BLAST_PROTECTION, 10);
        }
        if (to.getItem() == get("medic")) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, Integer.MAX_VALUE, 3, false, false));
        }
        if (to.getItem() == get("defender_iii")) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("concord")) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("horse")) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("spn")) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("atleti")) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Integer.MAX_VALUE, 3, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
        if (to.getItem() == get("nyyyaaaa")) {
            enchant(to, net.minecraft.world.item.enchantment.Enchantments.THORNS, 10);
        }
    }

    public static void onRemove(Player player, ItemStack from) {
        if (from.getItem() == get("marine")) {
            player.removeEffect(MobEffects.WATER_BREATHING);
            player.removeEffect(MobEffects.DOLPHINS_GRACE);
        }
        if (from.getItem() == get("medic")) {
            player.removeEffect(MobEffects.REGENERATION);
        }
        if (from.getItem() == get("defender_iii")) {
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("concord")) {
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("horse")) {
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("spn")) {
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("atleti")) {
            player.removeEffect(MobEffects.MOVEMENT_SPEED);
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("concord_h")) {
            player.removeEffect(MobEffects.REGENERATION);
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("gpnvg_h")) {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
        if (from.getItem() == get("ghost_h")) {
            player.removeEffect(MobEffects.NIGHT_VISION);
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
        if (from.getItem() == get("killa_h")) {
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
    }

    public static void enchant(ItemStack itemStack, Enchantment enchantment, int level) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (!enchantments.containsKey(enchantment)) {
            itemStack.enchant(enchantment, level);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Inventory inventory = player.getInventory();
        ItemStack helmet = inventory.armor.get(3);
        ItemStack chest = inventory.armor.get(2);
        ItemStack carried = player.containerMenu.getCarried();

        if (carried.getItem() instanceof BasicArmor) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(carried);
            if (!enchants.isEmpty()) {
                enchants.clear();
                EnchantmentHelper.setEnchantments(enchants, carried);
            }
        }

        for (ItemStack item : inventory.items) {
            if (item.getItem() instanceof BasicArmor) {
                Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(item);
                if (!enchants.isEmpty()) {
                    enchants.clear();
                    EnchantmentHelper.setEnchantments(enchants, item);
                }
            }
        }

        if (helmet.getItem() == get("gasmask_h")) {
            player.removeEffect(MobEffects.POISON);
            player.removeEffect(MobEffects.BLINDNESS);
            player.removeEffect(MobEffects.HUNGER);
            player.removeEffect(MobEffects.CONFUSION);
            player.removeEffect(MobEffects.WITHER);
            player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            player.removeEffect(MobEffects.WEAKNESS);
        }

        if (event.side.isServer()) {
            if (helmet.getItem() == get("medic_h")) {
                List<Entity> entities = player.level().getEntities(player, player.getBoundingBox().inflate(5));
                boolean flag = chest.getItem() == get("medic");

                for (Entity entity : entities) {
                    if (entity instanceof Player nearbyPlayer) {
                        if (!nearbyPlayer.hasEffect(MobEffects.REGENERATION)) {
                            nearbyPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, flag ? 4 : 3, false, false));
                        }
                    }
                }

                if (!player.hasEffect(MobEffects.REGENERATION)) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, flag ? 4 : 3, false, false));
                }
            }
        }
    }
}

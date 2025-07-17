package concord.effects;

import concord.common.items.BasicArmor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

import static concord.common.items.ItemRegistry.get;
import static concord.effects.EffectsConfig.*;

@Mod.EventBusSubscriber
public class EffectsManager {

    @SubscribeEvent
    public static void onEquipment(LivingEquipmentChangeEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            ItemStack to = event.getTo();
            ItemStack from = event.getFrom();

            if (event.getSlot() == EquipmentSlotType.HEAD) {
                applyConfigEffects(player, to, helmetEffects);
            }
            if (event.getSlot() == EquipmentSlotType.CHEST) {
                applyConfigEffects(player, to, chestEffects);
                applyEnchantments(to);
            }
            removeConfigEffects(player, from);
        }
    }

    private static void applyConfigEffects(PlayerEntity player, ItemStack stack,
                                           Map<String, List<EffectData>> map) {
        if (stack.isEmpty()) return;
        String id = stack.getItem().getRegistryName().getPath();
        List<EffectData> effects = map.get(id);
        if (effects != null) {
            for (EffectData data : effects) {
                player.addEffect(data.toInstance());
            }
        }
    }

    private static void removeConfigEffects(PlayerEntity player, ItemStack stack) {
        if (stack.isEmpty()) return;
        String id = stack.getItem().getRegistryName().getPath();

        List<EffectData> effects = helmetEffects.get(id);
        if (effects == null) effects = chestEffects.get(id);
        if (effects != null) {
            for (EffectData data : effects) {
                player.removeEffect(data.getEffect());
            }
        }
    }

    private static void applyEnchantments(ItemStack stack) {
        if (stack.isEmpty()) return;
        String id = stack.getItem().getRegistryName().getPath();

        if (blastProtected.contains(id)) {
            enchant(stack, Enchantments.BLAST_PROTECTION, 10);
        }
        if (thorned.contains(id)) {
            enchant(stack, Enchantments.THORNS, 10);
        }
    }

    private static void enchant(ItemStack stack, Enchantment enchantment, int level) {
        boolean needsEnchant = true;

        ListNBT tagList = stack.getEnchantmentTags();
        String enchantKey = enchantment.getRegistryName().toString();

        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).getAsString().contains(enchantKey)) {
                needsEnchant = false;
                break;
            }
        }

        if (needsEnchant) {
            stack.enchant(enchantment, level);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        PlayerInventory inv = player.inventory;

        ItemStack helmet = inv.armor.get(3);
        ItemStack chest = inv.armor.get(2);

        // Strip BasicArmor enchantments when not equipped
        if (inv.getCarried().getItem() instanceof BasicArmor) {
            if (!inv.getCarried().getEnchantmentTags().isEmpty()) {
                inv.getCarried().getEnchantmentTags().clear();
            }
        }

        for (ItemStack item : inv.items) {
            if (item.getItem() instanceof BasicArmor) {
                if (!item.getEnchantmentTags().isEmpty()) {
                    item.getEnchantmentTags().clear();
                }
            }
        }

        // Gas mask status effect removal
        if (!helmet.isEmpty() && helmet.getItem() == get("gasmask_h")) {
            player.removeEffect(Effects.POISON);
            player.removeEffect(Effects.BLINDNESS);
            player.removeEffect(Effects.HUNGER);
            player.removeEffect(Effects.CONFUSION);
            player.removeEffect(Effects.WITHER);
            player.removeEffect(Effects.MOVEMENT_SLOWDOWN);
            player.removeEffect(Effects.WEAKNESS);
        }

        // Medic helmet passive regen to nearby players
        if (event.side.isServer() && !helmet.isEmpty() && helmet.getItem() == get("medic_h")) {
            List<Entity> nearby = player.level.getEntities(player, player.getBoundingBox().inflate(5));
            boolean upgraded = !chest.isEmpty() && chest.getItem() == get("medic");

            for (Entity entity : nearby) {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity nearbyPlayer = (PlayerEntity) entity;
                    if (!nearbyPlayer.hasEffect(Effects.REGENERATION)) {
                        nearbyPlayer.addEffect(new EffectInstance(Effects.REGENERATION, 100, upgraded ? 4 : 3, false, false));
                    }
                }
            }

            if (!player.hasEffect(Effects.REGENERATION)) {
                player.addEffect(new EffectInstance(Effects.REGENERATION, 100, upgraded ? 4 : 3, false, false));
            }
        }
    }
}

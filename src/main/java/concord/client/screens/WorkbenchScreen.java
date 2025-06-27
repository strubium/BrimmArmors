package concord.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import concord.Concord;
import concord.client.ClientProxy;
import concord.client.render.IModel;
import concord.client.render.IRarity;
import concord.common.items.BasicArmor;
import concord.common.items.ConcordRarity;
import concord.common.network.packets.RequestCraftItem;
import concord.common.recipes.Ingredient;
import concord.common.recipes.ItemRecipe;
import concord.common.recipes.RecipesManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchScreen extends Screen {

    protected final Minecraft mc;
    protected final RecipesManager.CraftType craftType;

    protected final int imageWidth = 250;
    protected final int imageHeight = 180;
    protected int leftPos;
    protected int topPos;

    protected ArrayList<ItemRecipe> recipes;
    protected ItemRecipe itemRecipe;
    protected IModel renderModel;

    protected int craftSize;
    protected short currentIndex = 0;

    protected Button left;
    protected Button right;
    protected Button craft;
    protected final float speed = 0.01f;
    protected float prevAngle = 0f;
    protected float angle = 0f;
    protected float targetAngle = 0f;
    protected boolean rotating = false;
    protected long lastRotationTime = 0;
    protected final long rotationDelay = 800;

    public WorkbenchScreen(RecipesManager.CraftType craftType) {
        super(Component.literal("Workbench"));
        this.mc = Minecraft.getInstance();
        this.craftType = craftType;
        this.recipes = RecipesManager.getRecipe(craftType);
        this.craftSize = recipes.size();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.craft = this.addRenderableWidget(Button.builder(Component.literal(ChatFormatting.BOLD + "CRAFT"), (a) -> {
            Concord.network.sendToServer(new RequestCraftItem(craftType, currentIndex));
        }).bounds(leftPos + imageWidth / 2 - 40, topPos + imageHeight - 25, 80, 20).build());
        this.craft.active = false;

        this.left = this.addRenderableWidget(Button.builder(Component.literal(ChatFormatting.BOLD + "<"), (button) -> {
            if (currentIndex == 0) {
                button.active = false;
                return;
            }
            right.active = true;
            currentIndex--;
            if (currentIndex == 0) {
                button.active = false;
            }
        }).bounds(leftPos + imageWidth / 2 - 62, topPos + imageHeight - 25, 20, 20).build());
        this.left.active = false;

        this.right = this.addRenderableWidget(Button.builder(Component.literal(ChatFormatting.BOLD + ">"), (button) -> {
            if (currentIndex == craftSize - 1) {
                button.active = false;
                return;
            }
            left.active = true;
            currentIndex++;
            if (currentIndex == craftSize - 1) {
                button.active = false;
            }
        }).bounds(leftPos + imageWidth / 2 + 42, topPos + imageHeight - 25, 20, 20).build());
        this.right.active = craftSize > 1;
    }

    @Override
    public void tick() {
        if (recipes.isEmpty()) {
            this.craft.active = false;
            return;
        }

        this.itemRecipe = this.recipes.get(currentIndex);
        if (itemRecipe.result instanceof IModel model) {
            this.renderModel = model;
        } else if (itemRecipe.result instanceof BlockItem blockItem && blockItem.getBlock() instanceof IModel modelBlock) {
            this.renderModel = modelBlock;
        } else {
            this.renderModel = null;
        }

        if (itemRecipe != null) {
            boolean flag = true;
            for (Ingredient ingredient : itemRecipe.ingredients) {
                if (ingredient.count > getItemsCount(ingredient)) {
                    flag = false;
                    break;
                }
            }
            this.craft.active = flag;

            if (itemRecipe.result instanceof BasicArmor basicArmor) {
                EquipmentSlot type = basicArmor.type;
                if (type == EquipmentSlot.CHEST) {
                    updateRotation(0, 90, 180, 270);
                } else {
                    updateRotation(0, 90, 225, 0);
                }
            } else {
                updateRotation(0, 180, 360, 0);
            }
        }
    }

    public void updateRotation(float first, float second, float three, float four) {
        long currentTime = System.currentTimeMillis();

        if (!rotating && currentTime - lastRotationTime >= rotationDelay) {
            rotating = true;
            lastRotationTime = currentTime;
        }

        if (rotating && Math.abs(angle - targetAngle) < 0.01f) {
            rotating = false;
            lastRotationTime = currentTime;

            if (targetAngle == first) {
                targetAngle = second;
            } else if (targetAngle == second) {
                targetAngle = three;
            } else if (targetAngle == three) {
                targetAngle = four;
            } else if (targetAngle == four) {
                targetAngle = first;
            }
        }
    }

    private float lerp(float start, float end, float alpha) {
        return start + alpha * (end - start);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int x = this.leftPos;
        int y = this.topPos;

        PoseStack poseStack = guiGraphics.pose();

        if (rotating) {
            prevAngle = angle;
            angle = lerp(angle, targetAngle, speed);
        }

        fill(poseStack, x - 1, y - 1, x + imageWidth + 1, y + imageHeight + 1, 0xFF000000); // Black border
        fillGradient(poseStack, x, y, x + imageWidth, y + imageHeight, 0xFF720000, 0xFF4B0000); // Background gradient
        fill(poseStack, x + imageWidth / 2 - 36, y + 4, x + imageWidth / 2 + 36, y + 81, 0xFF000000); // Inner black box
        fillGradient(poseStack, x + imageWidth / 2 - 35, y + 5, x + imageWidth / 2 + 35, y + 80, 0xFF720000, 0xFF4B0000); // Inner gradient

        if (itemRecipe != null) {
            ItemStack itemStack = new ItemStack(itemRecipe.result);
            if (renderModel != null) {
                renderResult(guiGraphics, x + imageWidth / 2, y + 42, partialTick);
            } else {
                PoseStack stack = new PoseStack();
                stack.translate(x + imageWidth / 2f, y + 42, 200);
                stack.scale(50f, -50f, 50f);

                MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
                BakedModel model = mc.getItemRenderer().getModel(itemStack, mc.level, mc.player, 0);
                mc.getItemRenderer().render(itemStack, ItemDisplayContext.GUI, false, stack, buffer, 15728880, OverlayTexture.NO_OVERLAY, model);
                buffer.endBatch();
            }

            int tooltipOffsetY = 0;
            int i = 0;
            List<Component> tooltipFromItem = getTooltipFromItem(itemStack);

            if (itemRecipe.result instanceof IRarity rarityItem) {
                ConcordRarity rarity = rarityItem.getRarity();
                for (Component iTextComponent : tooltipFromItem) {
                    if (i == 0) {
                        guiGraphics.drawString(mc.font, Component.literal(rarity.color + iTextComponent.getString()), x + imageWidth / 2 - mc.font.width(iTextComponent) / 2, y + 82 + tooltipOffsetY, -1);
                        tooltipOffsetY += 10;
                        i++;
                        continue;
                    }
                    if (tooltipFromItem.size() <= 2) break;
                    if (i == 1) {
                        String formatting = ChatFormatting.ITALIC + rarity.color.toString();
                        String string = iTextComponent.getString();
                        if (string.length() > 40) {
                            int splitPos = string.lastIndexOf(" ", 40);
                            if (splitPos == -1) {
                                splitPos = 40;
                            }
                            String substring1 = string.substring(0, splitPos);
                            String substring2 = string.substring(splitPos).trim();
                            guiGraphics.drawString(mc.font, Component.literal(formatting + substring1), x + imageWidth / 2 - mc.font.width(substring1) / 2, y + 82 + tooltipOffsetY, -1);
                            guiGraphics.drawString(mc.font, Component.literal(formatting + substring2), x + imageWidth / 2 - mc.font.width(substring2) / 2, y + 82 + tooltipOffsetY + 10, -1);
                        } else {
                            guiGraphics.drawString(mc.font, Component.literal(formatting + iTextComponent.getString()), x + imageWidth / 2 - mc.font.width(iTextComponent) / 2, y + 82 + tooltipOffsetY, -1);
                        }
                    } else {
                        guiGraphics.drawString(mc.font, iTextComponent, x + imageWidth / 2 - mc.font.width(iTextComponent) / 2, y + 82 + tooltipOffsetY, -1);
                    }
                    tooltipOffsetY += 10;
                    i++;
                }
            } else {
                guiGraphics.drawString(mc.font, tooltipFromItem.get(0), x + imageWidth / 2 - mc.font.width(tooltipFromItem.get(0)) / 2, y + 82 + tooltipOffsetY, -1);
            }

            int ingredientXOffset = 0;
            int ingredientYOffset = 0;

            int j = 0;
            for (Ingredient ingredient : itemRecipe.ingredients) {
                if (j == 3 && itemRecipe.ingredients.length > 4) {
                    ingredientXOffset = 125;
                    ingredientYOffset = 0;
                }

                guiGraphics.renderItem(new ItemStack(ingredient.item, ingredient.count), x + imageWidth / 2 - 85 + ingredientXOffset, y + 5 + ingredientYOffset);
                int itemsCount = getItemsCount(ingredient);

                boolean flag = itemsCount >= ingredient.count;

                String formatted = String.format("%s/%s", flag ? ingredient.count : itemsCount, ingredient.count);
                guiGraphics.drawString(mc.font, Component.literal(formatted), x + imageWidth / 2 - 65 + ingredientXOffset, y + 10 + ingredientYOffset, flag ? 0xFF769146 : 0xFFB24C58);
                ingredientYOffset += 19;
                j++;
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    protected int getItemsCount(Ingredient ingredient) {
        return mc.player.getInventory().countItem(ingredient.item);
    }

    private List<Component> getTooltipFromItem(ItemStack stack) {
        TooltipFlag flag = mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
        return stack.getTooltipLines(mc.player, flag);
    }


    protected void renderResult(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        poseStack.translate(x, y, 200);
        if (renderModel != null) {
            renderModel.getTransform().WORKBENCH.setup(poseStack);
        }

        if (itemRecipe.result instanceof BlockItem) {
            poseStack.translate(0.5, -0.5, 0);
        }

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        ResourceLocation texture = renderModel.getTexture();
        int packedLight = 15728880;
        int packedOverlay = OverlayTexture.NO_OVERLAY;

        mc.getTextureManager().bindForSetup(texture);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        ClientProxy.getModel(renderModel.getModel()).renderAll(poseStack, buffer, texture, packedLight, packedOverlay);
        buffer.endBatch();

        poseStack.popPose();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    private void fill(PoseStack poseStack, int x1, int y1, int x2, int y2, int color) {
        if (x1 < x2) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            int j = y1;
            y1 = y2;
            y2 = j;
        }

        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Matrix4f matrix = poseStack.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, (float)x1, (float)y2, 0.0F).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, (float)x2, (float)y2, 0.0F).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, (float)x2, (float)y1, 0.0F).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, (float)x1, (float)y1, 0.0F).color(r, g, b, a).endVertex();
        Tesselator.getInstance().end();

        RenderSystem.disableBlend();
    }

    private void fillGradient(PoseStack poseStack, int x1, int y1, int x2, int y2, int startColor, int endColor) {
        float startAlpha = (float)(startColor >> 24 & 255) / 255.0F;
        float startRed = (float)(startColor >> 16 & 255) / 255.0F;
        float startGreen = (float)(startColor >> 8 & 255) / 255.0F;
        float startBlue = (float)(startColor & 255) / 255.0F;

        float endAlpha = (float)(endColor >> 24 & 255) / 255.0F;
        float endRed = (float)(endColor >> 16 & 255) / 255.0F;
        float endGreen = (float)(endColor >> 8 & 255) / 255.0F;
        float endBlue = (float)(endColor & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Matrix4f matrix = poseStack.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, x2, y1, 0.0F).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.vertex(matrix, x1, y1, 0.0F).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.vertex(matrix, x1, y2, 0.0F).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        buffer.vertex(matrix, x2, y2, 0.0F).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        Tesselator.getInstance().end();

        RenderSystem.disableBlend();
    }
}

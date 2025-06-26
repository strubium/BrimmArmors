package concord.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipFlag;

import net.minecraft.world.level.block.Block;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class WorkbenchScreen extends Screen {

    protected Minecraft mc;
    protected RecipesManager.CraftType craftType;

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
    protected float speed = 0.01f;
    protected float prevAngle = 0f;
    protected float angle = 0f;
    protected float targetAngle = 0f;
    protected boolean rotating = false;
    protected long lastRotationTime = 0;
    protected long rotationDelay = 800;

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
        this.craft = this.addButton(new Button(leftPos + imageWidth / 2 - 40, topPos + imageHeight - 25, 80, 20, Component.literal(ChatFormatting.BOLD + "CRAFT"), (a) -> {
            Concord.network.sendToServer(new RequestCraftItem(craftType, currentIndex));
        }));
        this.craft.active = false;
        this.left = this.addButton(new Button(leftPos + imageWidth / 2 - 62, topPos + imageHeight - 25, 20, 20, Component.literal(ChatFormatting.BOLD + "<"), (button) -> {
            if (currentIndex == 0) {
                button.active = false;
                return;
            }
            right.active = true;
            currentIndex--;
            if (currentIndex == 0) {
                button.active = false;
            }
        }));
        this.left.active = false;
        this.right = this.addButton(new Button(leftPos + imageWidth / 2 + 42, topPos + imageHeight - 25, 20, 20, Component.literal(ChatFormatting.BOLD + ">"), (button) -> {
            if (currentIndex == craftSize - 1) {
                button.active = false;
                return;
            }
            left.active = true;
            currentIndex++;
            if (currentIndex == craftSize - 1) {
                button.active = false;
            }
        }));
        this.right.active = craftSize > 1;
    }

    @Override
    public void tick() {
        this.itemRecipe = this.recipes.get(currentIndex);
        if (itemRecipe.result instanceof IModel) {
            this.renderModel = (IModel) itemRecipe.result;
        } else {
            if (itemRecipe.result instanceof BlockItem) {
                Block block = ((BlockItem) itemRecipe.result).getBlock();
                if (block instanceof IModel) {
                    this.renderModel = (IModel) block;
                }
            }
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
        }
        if (itemRecipe != null) {

            if (itemRecipe.result instanceof BasicArmor) {
                EquipmentSlot type = ((BasicArmor) itemRecipe.result).type;
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
                return;
            }
            if (targetAngle == second) {
                targetAngle = three;
                return;
            }
            if (targetAngle == three) {
                targetAngle = four;
                return;
            }
            if (targetAngle == four) {
                targetAngle = first;
                return;
            }
        }
    }

    private float lerp(float start, float end, float alpha) {
        return start + alpha * (end - start);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float particleTick) {
        int x = this.leftPos;
        int y = this.topPos;

        if (rotating) {
            prevAngle = angle;
            angle = lerp(angle, targetAngle, speed);
        }

        fill(poseStack, x - 1, y - 1, x + imageWidth + 1, y + imageHeight + 1, new Color(0x000000).getRGB());
        fillGradient(poseStack, x, y, x + imageWidth, y + imageHeight, new Color(0x720000).getRGB(), new Color(0x4B0000).getRGB());
        fill(poseStack, x + imageWidth / 2 - 36, y + 4, x + imageWidth / 2 + 36, y + 81, new Color(0x000000).getRGB());
        fillGradient(poseStack, x + imageWidth / 2 - 35, y + 5, x + imageWidth / 2 + 35, y + 80, new Color(0x720000).getRGB(), new Color(0x4B0000).getRGB());

        if (itemRecipe != null) {
            ItemStack itemStack = new ItemStack(itemRecipe.result);
            if (renderModel != null) {
                renderResult(x + imageWidth / 2, y + 42, particleTick);
            } else {
                PoseStack stack = new PoseStack();
                stack.translate(x + imageWidth / 2f, y + 42, 200);
                stack.scale(50f, -50f, 50f);

                float smoothAngle = prevAngle + (angle - prevAngle) * particleTick;
                Quaternion rotation = new Quaternion(0f, smoothAngle, 0f, true);
                stack.mulPose(rotation);

                IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                RenderHelper.setupForFlatItems();
                mc.getItemRenderer().render(itemStack, net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.GUI, false, stack, buffer, 15728880, OverlayTexture.NO_OVERLAY, mc.getItemRenderer().getModel(itemStack, null, null));
                buffer.endBatch();
            }

            int tooltipOffsetY = 0;
            int i = 0;
            List<Component> tooltipFromItem = getTooltipFromItem(itemStack);
            if (itemRecipe.result instanceof IRarity) {
                ConcordRarity rarity = ((IRarity) itemRecipe.result).getRarity();
                for (Component iTextComponent : tooltipFromItem) {
                    if (i == 0) {
                        mc.font.draw(poseStack, rarity.color + iTextComponent.getString(), x + imageWidth / 2f - font.width(iTextComponent) / 2f, y + 82 + tooltipOffsetY, -1);
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
                            mc.font.draw(poseStack, formatting + substring1, x + imageWidth / 2f - font.width(substring1) / 2f, y + 82 + tooltipOffsetY, -1);
                            mc.font.draw(poseStack, formatting + substring2, x + imageWidth / 2f - font.width(substring2) / 2f, y + 82 + tooltipOffsetY + 10, -1);
                        } else {
                            mc.font.draw(poseStack, formatting + iTextComponent.getString(), x + imageWidth / 2f - font.width(iTextComponent) / 2f, y + 82 + tooltipOffsetY, -1);
                        }
                    } else {
                        mc.font.draw(poseStack, iTextComponent, x + imageWidth / 2f - font.width(iTextComponent) / 2f, y + 82 + tooltipOffsetY, -1);
                    }
                    tooltipOffsetY += 10;
                    i++;
                }
            } else {
                mc.font.draw(poseStack, tooltipFromItem.get(0), x + imageWidth / 2f - font.width(tooltipFromItem.get(0)) / 2f, y + 82 + tooltipOffsetY, -1);
            }

            int ingredientXOffset = 0;
            int ingredientYOffset = 0;

            int j = 0;
            for (Ingredient ingredient : itemRecipe.ingredients) {
                if (j == 3 && itemRecipe.ingredients.length > 4) {
                    ingredientXOffset = 125;
                    ingredientYOffset = 0;
                }

                mc.getItemRenderer().renderGuiItem(new ItemStack(ingredient.item, ingredient.count), x + imageWidth / 2 - 85 + ingredientXOffset, y + 5 + ingredientYOffset);
                int itemsCount = getItemsCount(ingredient);

                boolean flag = itemsCount >= ingredient.count;

                String formatted = String.format("%s/%s", flag ? ingredient.count : itemsCount, ingredient.count);
                mc.font.draw(poseStack, formatted, x + imageWidth / 2f - 65 + ingredientXOffset, y + 10 + ingredientYOffset, flag ? new Color(0x769146).getRGB() : new Color(0xB24C58).getRGB());
                ingredientYOffset += 19;
                j++;
            }
        }
        super.render(poseStack, mouseX, mouseY, particleTick);
    }

    @SuppressWarnings("DataFlowIssue")
    protected int getItemsCount(Ingredient ingredient) {
        return mc.player.getInventory().countItem(ingredient.item);
    }

    private List<Component> getTooltipFromItem(ItemStack stack) {
        return stack.getTooltipLines(mc.player, mc.options.advancedItemTooltips ? ClientTooltipFlag.Advanced.INSTANCE : ClientTooltipFlag.Normal.INSTANCE);
    }

    protected void renderResult(int x, int y, float partialTicks) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();

        // Position in GUI
        poseStack.translate(x, y, 200);

        // Setup model transform (custom transform defined by your model)
        renderModel.getTransform().WORKBENCH.setup(poseStack);

        // Smooth rotation between previous and current angle
        float smoothAngle = prevAngle + (angle - prevAngle) * partialTicks;

        // Use Quaternion rotation
        Quaternion rotation = new Quaternion(0f, smoothAngle, 0f, true);
        poseStack.mulPose(rotation);

        if (itemRecipe.result instanceof BlockItem) {
            poseStack.translate(0.5, -0.5, 0);
        }

        // Bind texture for the model
        mc.getTextureManager().bind(renderModel.getTexture());

        // Enable blending and depth testing for proper rendering
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        // Render the model with the pose stack
        ClientProxy.getModel(renderModel.getModel()).renderAll(poseStack);

        // Cleanup
        poseStack.popPose();

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }
}

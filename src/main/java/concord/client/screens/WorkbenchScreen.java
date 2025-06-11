package concord.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
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
    protected int currentIndex = 0;

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
        super(new StringTextComponent("Workbench"));
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
        this.craft = this.addButton(new Button(leftPos + imageWidth / 2 - 40, topPos + imageHeight - 25, 80, 20, new StringTextComponent(TextFormatting.BOLD + "CRAFT"), (a) -> {
            Concord.network.sendToServer(new RequestCraftItem(craftType, currentIndex));
        }));
        this.craft.active = false;
        this.left = this.addButton(new Button(leftPos + imageWidth / 2 - 62, topPos + imageHeight - 25, 20, 20, new StringTextComponent(TextFormatting.BOLD + "<"), (button) -> {
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
        this.right = this.addButton(new Button(leftPos + imageWidth / 2 + 42, topPos + imageHeight - 25, 20, 20, new StringTextComponent(TextFormatting.BOLD + ">"), (button) -> {
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
                EquipmentSlotType type = ((BasicArmor) itemRecipe.result).type;
                if (type == EquipmentSlotType.CHEST) {
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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float particleTick) {
        int x = this.leftPos;
        int y = this.topPos;

        if (rotating) {
            prevAngle = angle;
            angle = lerp(angle, targetAngle, speed);
        }

        fill(matrixStack, x - 1, y - 1, x + imageWidth + 1, y + imageHeight + 1, new Color(0x000000).getRGB());
        fillGradient(matrixStack, x, y, x + imageWidth, y + imageHeight, new Color(0x720000).getRGB(), new Color(0x4B0000).getRGB());
        fill(matrixStack, x + imageWidth / 2 - 36, y + 4, x + imageWidth / 2 + 36, y + 81, new Color(0x000000).getRGB());
        fillGradient(matrixStack, x + imageWidth / 2 - 35, y + 5, x + imageWidth / 2 + 35, y + 80, new Color(0x720000).getRGB(), new Color(0x4B0000).getRGB());

        if (itemRecipe != null) {
            ItemStack itemStack = new ItemStack(itemRecipe.result);
            if (renderModel != null) {
                renderResult(x + imageWidth / 2, y + 42, particleTick);
            } else {
                MatrixStack matrixstack = new MatrixStack();
                matrixstack.translate(x + imageWidth / 2f, y + 42, 200);
                matrixstack.scale(50f, -50f, 50f);
                matrixstack.mulPose(Vector3f.YP.rotationDegrees(prevAngle + (angle - prevAngle) * particleTick));
                IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
                RenderHelper.setupForFlatItems();
                mc.getItemRenderer().render(itemStack, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, mc.getItemRenderer().getModel(itemStack, null, null));
                irendertypebuffer$impl.endBatch();
            }

            int tooltipOffsetY = 0;
            int i = 0;
            List<ITextComponent> tooltipFromItem = getTooltipFromItem(itemStack);
            if (itemRecipe.result instanceof IRarity) {
                ConcordRarity rarity = ((IRarity) itemRecipe.result).getRarity();
                for (ITextComponent iTextComponent : tooltipFromItem) {
                    if (i == 0) {
                        mc.font.draw(matrixStack, rarity.color + iTextComponent.getString(), x + imageWidth / 2f - font.width(iTextComponent) / 2f, y + 82 + tooltipOffsetY, -1);
                        tooltipOffsetY += 10;
                        i++;
                        continue;
                    }
                    if (tooltipFromItem.size() <= 2) break;
                    if (i == 1) {
                        String formatting = TextFormatting.ITALIC + rarity.color.toString();
                        String string = iTextComponent.getString();
                        if (string.length() > 40) {
                            int splitPos = string.lastIndexOf(" ", 40);
                            if (splitPos == -1) {
                                splitPos = 40;
                            }
                            String substring1 = string.substring(0, splitPos);
                            String substring2 = string.substring(splitPos).trim();
                            mc.font.draw(matrixStack, formatting + substring1, x + imageWidth / 2f - font.width(substring1) / 2f, y + 82 + tooltipOffsetY, -1);
                            mc.font.draw(matrixStack, formatting + substring2, x + imageWidth / 2f - font.width(substring2) / 2f, y + 82 + tooltipOffsetY + 10, -1);
                        } else {
                            mc.font.draw(matrixStack, formatting + iTextComponent.getString(), x + imageWidth / 2f - font.width(iTextComponent) / 2f, y + 82 + tooltipOffsetY, -1);
                        }
                    } else {
                        mc.font.draw(matrixStack, iTextComponent, x + imageWidth / 2f - font.width(iTextComponent) / 2f, y + 82 + tooltipOffsetY, -1);
                    }
                    tooltipOffsetY += 10;
                    i++;
                }
            } else {
                mc.font.draw(matrixStack, tooltipFromItem.get(0), x + imageWidth / 2f - font.width(tooltipFromItem.get(0)) / 2f, y + 82 + tooltipOffsetY, -1);
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
                mc.font.draw(matrixStack, formatted, x + imageWidth / 2f - 65 + ingredientXOffset, y + 10 + ingredientYOffset, flag ? new Color(0x769146).getRGB() : new Color(0xB24C58).getRGB());
                ingredientYOffset += 19;
                j++;
            }
        }
        super.render(matrixStack, mouseX, mouseY, particleTick);
    }

    @SuppressWarnings("DataFlowIssue")
    protected int getItemsCount(Ingredient ingredient) {
        return mc.player.inventory.countItem(ingredient.item);
    }

    protected void renderResult(int x, int y, float partialTicks) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.pushPose();

        // Position in GUI
        matrixStack.translate(x, y, 200);

        // Setup model transform (custom transform defined by your model)
        renderModel.getTransform().WORKBENCH.setup(matrixStack);

        // Smooth rotation between previous and current angle
        float smoothAngle = prevAngle + (angle - prevAngle) * partialTicks;
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(smoothAngle));

        if (itemRecipe.result instanceof BlockItem) {
            matrixStack.translate(0.5, -0.5, 0);
        }

        // Bind texture for the model
        mc.getTextureManager().bind(renderModel.getTexture());

        // Enable blending and depth testing for proper rendering
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        // Render the model with the matrix stack
        ClientProxy.getModel(renderModel.getModel()).renderAll(matrixStack);

        // Cleanup
        matrixStack.popPose();

        // Disable blend and depth test if necessary (optional depending on render context)
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

}
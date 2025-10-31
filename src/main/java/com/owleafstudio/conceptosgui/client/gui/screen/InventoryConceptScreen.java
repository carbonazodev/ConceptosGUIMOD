package com.owleafstudio.conceptosgui.client.gui.screen;

import com.owleafstudio.conceptosgui.ConceptosGUI;
import com.owleafstudio.conceptosgui.container.CustomInventoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class InventoryConceptScreen extends AbstractContainerScreen<CustomInventoryMenu> {

    public InventoryConceptScreen(CustomInventoryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 176;
        this.imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        renderVanillaBackground(graphics, x, y);

        if (this.menu.isProcessing()) {
            int progress = this.menu.getScaledProgress();
            graphics.fill(x + 79, y + 34, x + 79 + progress, y + 34 + 16, 0xFF00FF00);
        }
    }

    private void renderVanillaBackground(GuiGraphics graphics, int x, int y) {
        graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFFC6C6C6);

        graphics.fill(x, y, x + this.imageWidth - 1, y + 1, 0xFFFFFFFF);
        graphics.fill(x, y, x + 1, y + this.imageHeight - 1, 0xFFFFFFFF);
        graphics.fill(x + 1, y + this.imageHeight - 1, x + this.imageWidth, y + this.imageHeight, 0xFF555555);
        graphics.fill(x + this.imageWidth - 1, y + 1, x + this.imageWidth, y + this.imageHeight - 1, 0xFF555555);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int slotX = x + 61 + j * 18;
                int slotY = y + 16 + i * 18;
                renderSlot(graphics, slotX, slotY);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int slotX = x + 7 + j * 18;
                int slotY = y + 83 + i * 18;
                renderSlot(graphics, slotX, slotY);
            }
        }

        for (int i = 0; i < 9; i++) {
            int slotX = x + 7 + i * 18;
            int slotY = y + 141;
            renderSlot(graphics, slotX, slotY);
        }
    }

    private void renderSlot(GuiGraphics graphics, int x, int y) {
        graphics.fill(x, y, x + 18, y + 18, 0xFF8B8B8B);
        graphics.fill(x, y, x + 17, y + 1, 0xFF373737);
        graphics.fill(x, y + 1, x + 1, y + 17, 0xFF373737);
        graphics.fill(x + 17, y + 1, x + 18, y + 18, 0xFFFFFFFF);
        graphics.fill(x + 1, y + 17, x + 17, y + 18, 0xFFFFFFFF);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
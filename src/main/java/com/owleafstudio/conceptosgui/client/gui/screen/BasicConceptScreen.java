package com.owleafstudio.conceptosgui.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BasicConceptScreen extends Screen {
    private EditBox textField;
    private Button actionButton;
    private Checkbox checkBox;
    private int guiLeft, guiTop;
    private final int xSize = 256;
    private final int ySize = 166;

    public BasicConceptScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.textField = new EditBox(this.font, guiLeft + 10, guiTop + 30, 150, 20,
                Component.literal("Input"));
        this.textField.setMaxLength(50);
        this.textField.setValue("Texto de ejemplo");
        this.addRenderableWidget(textField);

        this.actionButton = Button.builder(
                Component.literal("Click Me!"),
                button -> this.onButtonClick()
        ).bounds(guiLeft + 10, guiTop + 60, 100, 20).build();
        this.addRenderableWidget(actionButton);

        this.checkBox = new Checkbox(guiLeft + 10, guiTop + 90, 150, 20,
                Component.literal("Opción de ejemplo"), false);
        this.addRenderableWidget(checkBox);

        this.addRenderableWidget(Button.builder(
                Component.literal("Éxito"),
                button -> sendMessage("Botón de éxito presionado!", 0x00FF00)
        ).bounds(guiLeft + 10, guiTop + 120, 50, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Advertencia"),
                button -> sendMessage("Botón de advertencia!", 0xFFAA00)
        ).bounds(guiLeft + 70, guiTop + 120, 70, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Error"),
                button -> sendMessage("Botón de error!", 0xFF0000)
        ).bounds(guiLeft + 150, guiTop + 120, 50, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("X"),
                button -> this.onClose()
        ).bounds(guiLeft + xSize - 25, guiTop + 5, 20, 20).build());
    }

    private void onButtonClick() {
        sendMessage("Botón presionado! Texto: " + textField.getValue(), 0xFFFFFF);
    }

    private void sendMessage(String message, int color) {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.sendSystemMessage(
                    Component.literal(message).withStyle(style -> style.withColor(color))
            );
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        renderVanillaBackground(graphics);
        graphics.drawCenteredString(this.font, this.title,
                guiLeft + xSize / 2, guiTop + 8, 0x404040);
        graphics.fill(guiLeft + 10, guiTop + 25, guiLeft + xSize - 10, guiTop + 26, 0xFF888888);

        super.render(graphics, mouseX, mouseY, partialTick);

        if (isHovering(10, 60, 100, 20, mouseX, mouseY)) {
            graphics.renderTooltip(this.font,
                    Component.literal("Este es un tooltip vanilla!"), mouseX, mouseY);
        }

        graphics.drawString(this.font,
                Component.literal("Checkbox estado: " + (checkBox.selected() ? "ON" : "OFF")),
                guiLeft + 170, guiTop + 95, checkBox.selected() ? 0x00FF00 : 0xFF0000);
    }

    private void renderVanillaBackground(GuiGraphics graphics) {
        graphics.fill(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, 0xFFC6C6C6);
        graphics.fill(guiLeft - 1, guiTop - 1, guiLeft + xSize + 1, guiTop, 0xFF000000);
        graphics.fill(guiLeft - 1, guiTop + ySize, guiLeft + xSize + 1, guiTop + ySize + 1, 0xFF000000);
        graphics.fill(guiLeft - 1, guiTop, guiLeft, guiTop + ySize, 0xFF000000);
        graphics.fill(guiLeft + xSize, guiTop, guiLeft + xSize + 1, guiTop + ySize, 0xFF000000);


        graphics.fill(guiLeft, guiTop, guiLeft + xSize - 1, guiTop + 1, 0xFFFFFFFF);
        graphics.fill(guiLeft, guiTop + 1, guiLeft + 1, guiTop + ySize - 1, 0xFFFFFFFF);

        graphics.fill(guiLeft + 1, guiTop + ySize - 1, guiLeft + xSize, guiTop + ySize, 0xFF555555);
        graphics.fill(guiLeft + xSize - 1, guiTop + 1, guiLeft + xSize, guiTop + ySize - 1, 0xFF555555);
    }

    private boolean isHovering(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= guiLeft + x && mouseX <= guiLeft + x + width &&
                mouseY >= guiTop + y && mouseY <= guiTop + y + height;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
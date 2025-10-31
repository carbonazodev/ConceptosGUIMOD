package com.owleafstudio.conceptosgui.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class AnimatedConceptScreen extends Screen {
    private int tickCount = 0;
    private float rotation = 0;
    private float scale = 1.0f;
    private float bounce = 0;
    private boolean expanding = true;

    private int guiLeft, guiTop;
    private final int xSize = 256;
    private final int ySize = 166;

    public AnimatedConceptScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.addRenderableWidget(new AnimatedButton(
                guiLeft + 20, guiTop + 40, 100, 20,
                Component.literal("Botón Animado"),
                button -> {}
        ));

        this.addRenderableWidget(Button.builder(
                Component.literal("Reset Animación"),
                button -> {
                    tickCount = 0;
                    rotation = 0;
                    scale = 1.0f;
                    bounce = 0;
                    expanding = true;
                }
        ).bounds(guiLeft + 20, guiTop + 120, 100, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Cerrar"),
                button -> this.onClose()
        ).bounds(guiLeft + 130, guiTop + 120, 100, 20).build());
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;
        rotation = (rotation + 2) % 360;

        if (expanding) {
            scale += 0.01f;
            if (scale >= 1.2f) expanding = false;
        } else {
            scale -= 0.01f;
            if (scale <= 0.8f) expanding = true;
        }

        bounce = Math.abs(Mth.sin(tickCount * 0.1f)) * 10;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        renderAnimatedBackground(graphics);

        graphics.pose().pushPose();
        graphics.pose().translate(guiLeft + xSize / 2, guiTop + 20, 0);
        graphics.pose().scale(scale, scale, 1);
        graphics.drawCenteredString(this.font, this.title, 0, 0,
                Mth.hsvToRgb(tickCount / 100f, 1.0f, 1.0f));
        graphics.pose().popPose();

        drawRotatingCircle(graphics, guiLeft + xSize / 2, guiTop + 80, 30);

        drawAnimatedBars(graphics);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderAnimatedBackground(GuiGraphics graphics) {
        int color1 = Mth.hsvToRgb((tickCount % 360) / 360f, 0.3f, 0.8f);
        int color2 = Mth.hsvToRgb(((tickCount + 180) % 360) / 360f, 0.3f, 0.8f);

        graphics.fillGradient(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize,
                color1 | 0xFF000000, color2 | 0xFF000000);

        graphics.fill(guiLeft - 2, guiTop - 2, guiLeft + xSize + 2, guiTop, 0xFF000000);
        graphics.fill(guiLeft - 2, guiTop + ySize, guiLeft + xSize + 2, guiTop + ySize + 2, 0xFF000000);
        graphics.fill(guiLeft - 2, guiTop, guiLeft, guiTop + ySize, 0xFF000000);
        graphics.fill(guiLeft + xSize, guiTop, guiLeft + xSize + 2, guiTop + ySize, 0xFF000000);
    }

    private void drawRotatingCircle(GuiGraphics graphics, int centerX, int centerY, int radius) {
        int segments = 20;
        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (2 * Math.PI * i / segments) + rotation * 0.01745f;
            float angle2 = (float) (2 * Math.PI * (i + 1) / segments) + rotation * 0.01745f;

            int x1 = (int) (centerX + radius * Math.cos(angle1));
            int y1 = (int) (centerY + radius * Math.sin(angle1));
            int x2 = (int) (centerX + radius * Math.cos(angle2));
            int y2 = (int) (centerY + radius * Math.sin(angle2));

            int color = Mth.hsvToRgb((i + tickCount) / (float)segments, 1.0f, 1.0f) | 0xFF000000;

            graphics.fill(x1, y1, x2 + 1, y2 + 1, color);
        }
    }

    private void drawAnimatedBars(GuiGraphics graphics) {
        int barY = guiTop + 70;
        for (int i = 0; i < 5; i++) {
            float height = Math.abs(Mth.sin((tickCount + i * 10) * 0.05f)) * 20 + 5;
            int color = Mth.hsvToRgb(i / 5f, 0.8f, 0.9f) | 0xFF000000;

            graphics.fill(
                    guiLeft + 160 + i * 15,
                    (int)(barY + 20 - height),
                    guiLeft + 170 + i * 15,
                    barY + 20,
                    color
            );
        }
    }

    class AnimatedButton extends Button {
        private float hoverAnimation = 0;

        public AnimatedButton(int x, int y, int width, int height, Component message, OnPress onPress) {
            super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            if (this.isHovered) {
                hoverAnimation = Math.min(hoverAnimation + 0.1f, 1.0f);
            } else {
                hoverAnimation = Math.max(hoverAnimation - 0.1f, 0.0f);
            }

            int baseColor = 0xFF4444FF;
            int hoverColor = 0xFF8888FF;
            int color = interpolateColor(baseColor, hoverColor, hoverAnimation);

            graphics.pose().pushPose();
            float scale = 1.0f + hoverAnimation * 0.1f;
            graphics.pose().translate(getX() + width / 2, getY() + height / 2, 0);
            graphics.pose().scale(scale, scale, 1);

            graphics.fill(-width / 2, -height / 2, width / 2, height / 2, color);
            graphics.drawCenteredString(font, getMessage(), 0, -4, 0xFFFFFF);

            graphics.pose().popPose();
        }

        private int interpolateColor(int color1, int color2, float factor) {
            int r1 = (color1 >> 16) & 0xFF;
            int g1 = (color1 >> 8) & 0xFF;
            int b1 = color1 & 0xFF;
            int r2 = (color2 >> 16) & 0xFF;
            int g2 = (color2 >> 8) & 0xFF;
            int b2 = color2 & 0xFF;

            int r = (int) (r1 + (r2 - r1) * factor);
            int g = (int) (g1 + (g2 - g1) * factor);
            int b = (int) (b1 + (b2 - b1) * factor);

            return 0xFF000000 | (r << 16) | (g << 8) | b;
        }
    }
}
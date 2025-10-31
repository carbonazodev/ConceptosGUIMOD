package com.owleafstudio.conceptosgui.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ProgressConceptScreen extends Screen {
    private float[] progress = new float[6];
    private boolean[] isActive = new boolean[6];
    private int tickCount = 0;

    private int guiLeft, guiTop;
    private final int xSize = 300;
    private final int ySize = 200;

    public ProgressConceptScreen(Component title) {
        super(title);
        for (int i = 0; i < progress.length; i++) {
            progress[i] = 0;
            isActive[i] = false;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.addRenderableWidget(Button.builder(
                Component.literal("Iniciar Todo"),
                button -> {
                    for (int i = 0; i < isActive.length; i++) {
                        isActive[i] = true;
                    }
                }
        ).bounds(guiLeft + 10, guiTop + 160, 80, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Pausar Todo"),
                button -> {
                    for (int i = 0; i < isActive.length; i++) {
                        isActive[i] = false;
                    }
                }
        ).bounds(guiLeft + 95, guiTop + 160, 80, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Resetear"),
                button -> {
                    for (int i = 0; i < progress.length; i++) {
                        progress[i] = 0;
                        isActive[i] = false;
                    }
                }
        ).bounds(guiLeft + 180, guiTop + 160, 60, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Cerrar"),
                button -> this.onClose()
        ).bounds(guiLeft + 245, guiTop + 160, 45, 20).build());
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;

        for (int i = 0; i < progress.length; i++) {
            if (isActive[i]) {
                float speed = 0.005f * (i + 1);
                progress[i] = Math.min(progress[i] + speed, 1.0f);

                if (progress[i] >= 1.0f) {
                    progress[i] = 0;
                }
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        renderVanillaPanel(graphics, guiLeft, guiTop, xSize, ySize);

        graphics.drawCenteredString(this.font, this.title,
                guiLeft + xSize / 2, guiTop + 10, 0xFFFFFF);

        int startY = guiTop + 30;

        drawBasicProgressBar(graphics, guiLeft + 10, startY, 280, 10, progress[0], "Básica");

        drawSegmentedProgressBar(graphics, guiLeft + 10, startY + 25, 280, 10, progress[1], "Segmentada", 10);

        drawGradientProgressBar(graphics, guiLeft + 10, startY + 50, 280, 10, progress[2], "Gradiente");

        drawCircularProgress(graphics, guiLeft + 40, startY + 90, 20, progress[3], "Circular");

        drawXPStyleBar(graphics, guiLeft + 100, startY + 75, 150, 10, progress[4], "Estilo XP");

        drawPulsingProgressBar(graphics, guiLeft + 100, startY + 100, 150, 10, progress[5], "Pulsante");

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawBasicProgressBar(GuiGraphics graphics, int x, int y, int width, int height,
                                      float progress, String label) {

        graphics.drawString(this.font, label + ": " + Math.round(progress * 100) + "%",
                x, y - 10, 0xFFFFFF);


        graphics.fill(x, y, x + width, y + height, 0xFF000000);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF444444);


        int progressWidth = (int)((width - 2) * progress);
        if (progressWidth > 0) {
            graphics.fill(x + 1, y + 1, x + 1 + progressWidth, y + height - 1, 0xFF00FF00);
        }
    }

    private void drawSegmentedProgressBar(GuiGraphics graphics, int x, int y, int width, int height,
                                          float progress, String label, int segments) {
        graphics.drawString(this.font, label + ": " + Math.round(progress * 100) + "%",
                x, y - 10, 0xFFFFFF);

        int segmentWidth = width / segments;
        int filledSegments = (int)(segments * progress);

        for (int i = 0; i < segments; i++) {
            int segX = x + i * segmentWidth;

            graphics.fill(segX, y, segX + segmentWidth - 2, y + height, 0xFF000000);
            graphics.fill(segX + 1, y + 1, segX + segmentWidth - 3, y + height - 1, 0xFF444444);


            if (i < filledSegments) {
                graphics.fill(segX + 1, y + 1, segX + segmentWidth - 3, y + height - 1, 0xFF00AAFF);
            }
        }
    }

    private void drawGradientProgressBar(GuiGraphics graphics, int x, int y, int width, int height,
                                         float progress, String label) {
        graphics.drawString(this.font, label + ": " + Math.round(progress * 100) + "%",
                x, y - 10, 0xFFFFFF);


        graphics.fill(x, y, x + width, y + height, 0xFF000000);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF222222);


        int progressWidth = (int)((width - 2) * progress);
        if (progressWidth > 0) {
            graphics.fillGradient(x + 1, y + 1, x + 1 + progressWidth, y + height - 1,
                    0xFFFF0000, 0xFFFFFF00);
        }
    }

    private void drawCircularProgress(GuiGraphics graphics, int centerX, int centerY, int radius,
                                      float progress, String label) {
        graphics.drawString(this.font, label, centerX - 20, centerY - radius - 10, 0xFFFFFF);

        int segments = 32;
        int filledSegments = (int)(segments * progress);

        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (2 * Math.PI * i / segments - Math.PI / 2);
            float angle2 = (float) (2 * Math.PI * (i + 1) / segments - Math.PI / 2);

            int x1 = (int) (centerX + radius * Math.cos(angle1));
            int y1 = (int) (centerY + radius * Math.sin(angle1));
            int x2 = (int) (centerX + radius * Math.cos(angle2));
            int y2 = (int) (centerY + radius * Math.sin(angle2));

            int color = i < filledSegments ? 0xFF00FF00 : 0xFF444444;


            graphics.fill(Math.min(x1, x2), Math.min(y1, y2),
                    Math.max(x1, x2) + 1, Math.max(y1, y2) + 1, color);
        }


        graphics.drawCenteredString(this.font, Math.round(progress * 100) + "%",
                centerX, centerY - 4, 0xFFFFFF);
    }

    private void drawXPStyleBar(GuiGraphics graphics, int x, int y, int width, int height,
                                float progress, String label) {
        graphics.drawString(this.font, label, x, y - 10, 0xFFFFFF);


        graphics.fill(x, y, x + width, y + height, 0xFF000000);


        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF002200);


        int progressWidth = (int)((width - 2) * progress);
        if (progressWidth > 0) {

            graphics.fill(x + 1, y + 1, x + 1 + progressWidth, y + height - 1, 0xFF80FF20);

            graphics.fill(x + 1, y + 1, x + 1 + progressWidth, y + 2, 0xFFB0FF50);
        }
    }

    private void drawPulsingProgressBar(GuiGraphics graphics, int x, int y, int width, int height,
                                        float progress, String label) {
        graphics.drawString(this.font, label, x, y - 10, 0xFFFFFF);


        float pulse = (Mth.sin(tickCount * 0.1f) + 1) / 2;
        int pulseColor = interpolateColor(0xFF0080FF, 0xFF00FFFF, pulse);


        graphics.fill(x, y, x + width, y + height, 0xFF000000);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF222222);


        int progressWidth = (int)((width - 2) * progress);
        if (progressWidth > 0) {
            graphics.fill(x + 1, y + 1, x + 1 + progressWidth, y + height - 1, pulseColor);
        }
    }

    private void renderVanillaPanel(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, x + width, y + height, 0xFFC6C6C6);
        graphics.fill(x, y, x + width - 1, y + 1, 0xFFFFFFFF);
        graphics.fill(x, y, x + 1, y + height - 1, 0xFFFFFFFF);
        graphics.fill(x + 1, y + height - 1, x + width, y + height, 0xFF555555);
        graphics.fill(x + width - 1, y + 1, x + width, y + height - 1, 0xFF555555);
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
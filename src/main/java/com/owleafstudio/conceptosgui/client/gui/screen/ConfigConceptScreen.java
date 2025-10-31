package com.owleafstudio.conceptosgui.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ConfigConceptScreen extends Screen {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    private boolean enableFeature = true;
    private double sliderValue = 0.5;
    private int selectedOption = 0;
    private final List<ConfigOption> options = new ArrayList<>();

    private int guiLeft, guiTop;
    private final int xSize = 300;
    private final int ySize = 240;

    public ConfigConceptScreen(Component title) {
        super(title);
        initOptions();
    }

    private void initOptions() {
        options.add(new ConfigOption("Modo PvP", Items.DIAMOND_SWORD, true));
        options.add(new ConfigOption("Partículas", Items.BLAZE_POWDER, true));
        options.add(new ConfigOption("Sonidos", Items.NOTE_BLOCK, false));
        options.add(new ConfigOption("Autoguardado", Items.BOOK, true));
        options.add(new ConfigOption("Mostrar FPS", Items.CLOCK, false));
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        int y = guiTop + 35;

        for (ConfigOption option : options) {
            Checkbox checkbox = new Checkbox(
                    guiLeft + 30, y, 150, 20,
                    Component.literal(option.name),
                    option.enabled
            ) {
                @Override
                public void onPress() {
                    super.onPress();
                    option.enabled = this.selected();
                    sendMessage("§e" + option.name + ": " + (option.enabled ? "§aActivado" : "§cDesactivado"));
                }
            };
            this.addRenderableWidget(checkbox);
            y += 25;
        }

        y += 10;

        VolumeSlider volumeSlider = new VolumeSlider(
                guiLeft + 30, y, 240, 20,
                Component.literal("Volumen: "),
                sliderValue
        );
        this.addRenderableWidget(volumeSlider);
        y += 30;

        String[] difficulties = {"Pacífico", "Fácil", "Normal", "Difícil"};
        for (int i = 0; i < difficulties.length; i++) {
            final int diff = i;
            Button diffButton = Button.builder(
                    Component.literal(difficulties[i]),
                    button -> {
                        selectedOption = diff;
                        sendMessage("§6Dificultad: §f" + difficulties[diff]);
                    }
            ).bounds(guiLeft + 30 + (i * 65), y, 60, 20).build();
            this.addRenderableWidget(diffButton);
        }
        y += 30;

        this.addRenderableWidget(Button.builder(
                Component.literal("§aGuardar"),
                button -> saveConfig()
        ).bounds(guiLeft + 30, y, 80, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("§eRestaurar"),
                button -> resetToDefaults()
        ).bounds(guiLeft + 115, y, 80, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("§cCancelar"),
                button -> this.onClose()
        ).bounds(guiLeft + 200, y, 70, 20).build());
    }

    private void saveConfig() {
        sendMessage("§a¡Configuración guardada exitosamente!");
        this.onClose();
    }

    private void resetToDefaults() {
        this.enableFeature = true;
        this.sliderValue = 0.5;
        this.selectedOption = 0;
        initOptions();
        this.init();
        sendMessage("§e¡Configuración restaurada a valores por defecto!");
    }

    private void sendMessage(String message) {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.sendSystemMessage(Component.literal(message));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        renderVanillaConfigPanel(graphics);


        graphics.drawCenteredString(this.font,
                Component.literal(" Configuración"),
                guiLeft + xSize / 2, guiTop + 10, 0xFFFFFF);

        graphics.fill(guiLeft + 20, guiTop + 28, guiLeft + xSize - 20, guiTop + 29, 0xFF555555);

        int y = guiTop + 35;
        for (ConfigOption option : options) {
            graphics.renderItem(new ItemStack(option.icon), guiLeft + 10, y);
            y += 25;
        }

        graphics.drawString(this.font,
                Component.literal("Valor: " + DECIMAL_FORMAT.format(sliderValue * 100) + "%"),
                guiLeft + 30, guiTop + 175, 0xAAAAAA);

        String[] diffColors = {"§b", "§a", "§e", "§c"};
        String[] difficulties = {"Pacífico", "Fácil", "Normal", "Difícil"};
        graphics.drawString(this.font,
                Component.literal("Dificultad actual: " + diffColors[selectedOption] + difficulties[selectedOption]),
                guiLeft + 30, guiTop + 195, 0xFFFFFF);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderVanillaConfigPanel(GuiGraphics graphics) {
        graphics.fill(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, 0xFFC6C6C6);
        graphics.fill(guiLeft, guiTop, guiLeft + xSize, guiTop + 2, 0xFFFFFFFF);
        graphics.fill(guiLeft, guiTop, guiLeft + 2, guiTop + ySize, 0xFFFFFFFF);
        graphics.fill(guiLeft + 2, guiTop + ySize - 2, guiLeft + xSize, guiTop + ySize, 0xFF555555);
        graphics.fill(guiLeft + xSize - 2, guiTop + 2, guiLeft + xSize, guiTop + ySize - 2, 0xFF555555);
        graphics.fill(guiLeft - 1, guiTop - 1, guiLeft + xSize + 1, guiTop, 0xFF000000);
        graphics.fill(guiLeft - 1, guiTop + ySize, guiLeft + xSize + 1, guiTop + ySize + 1, 0xFF000000);
        graphics.fill(guiLeft - 1, guiTop, guiLeft, guiTop + ySize, 0xFF000000);
        graphics.fill(guiLeft + xSize, guiTop, guiLeft + xSize + 1, guiTop + ySize, 0xFF000000);
    }

    class VolumeSlider extends AbstractSliderButton {
        public VolumeSlider(int x, int y, int width, int height, Component prefix, double value) {
            super(x, y, width, height, prefix, value);
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            String volume = DECIMAL_FORMAT.format(this.value * 100);
            String icon = "🔊";
            if (this.value < 0.01) icon = "🔇";
            else if (this.value < 0.33) icon = "🔈";
            else if (this.value < 0.66) icon = "🔉";

            this.setMessage(Component.literal(icon + " Volumen: " + volume + "%"));
        }

        @Override
        protected void applyValue() {
            sliderValue = this.value;
        }
    }

    static class ConfigOption {
        String name;
        net.minecraft.world.item.Item icon;
        boolean enabled;

        ConfigOption(String name, net.minecraft.world.item.Item icon, boolean enabled) {
            this.name = name;
            this.icon = icon;
            this.enabled = enabled;
        }
    }
}
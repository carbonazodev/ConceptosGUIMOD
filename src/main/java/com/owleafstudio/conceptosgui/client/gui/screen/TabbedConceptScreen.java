package com.owleafstudio.conceptosgui.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.ArrayList;
import java.util.List;

public class TabbedConceptScreen extends Screen {
    private int currentTab = 0;
    private List<TabButton> tabs = new ArrayList<>();
    private int guiLeft, guiTop;
    private final int xSize = 256;
    private final int ySize = 200;

    private final ItemStack[] tabIcons = {
            new ItemStack(Items.DIAMOND_SWORD),
            new ItemStack(Items.REDSTONE),
            new ItemStack(Items.BOOK),
            new ItemStack(Items.EMERALD)
    };

    public TabbedConceptScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        tabs.clear();

        String[] tabNames = {"Combate", "Redstone", "Información", "Comercio"};
        for (int i = 0; i < 4; i++) {
            final int tabIndex = i;
            TabButton tab = new TabButton(
                    guiLeft + (i * 50) + 10, guiTop - 28, 48, 28,
                    Component.literal(tabNames[i]),
                    button -> switchTab(tabIndex),
                    i == currentTab
            );
            tabs.add(tab);
            this.addRenderableWidget(tab);
        }

        updateTabContent();
    }

    private void switchTab(int tabIndex) {
        this.currentTab = tabIndex;
        for (int i = 0; i < tabs.size(); i++) {
            tabs.get(i).setSelected(i == tabIndex);
        }
        updateTabContent();
    }

    private void updateTabContent() {
        this.clearWidgets();
        tabs.forEach(this::addRenderableWidget);
        switch (currentTab) {
            case 0 -> addCombatTabContent();
            case 1 -> addRedstoneTabContent();
            case 2 -> addInfoTabContent();
            case 3 -> addTradeTabContent();
        }
    }

    private void addCombatTabContent() {
        this.addRenderableWidget(Button.builder(
                Component.literal("⚔ Atacar"),
                button -> sendMessage("¡Atacando!")
        ).bounds(guiLeft + 20, guiTop + 40, 100, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("🛡 Defender"),
                button -> sendMessage("¡Defendiendo!")
        ).bounds(guiLeft + 20, guiTop + 70, 100, 20).build());
    }

    private void addRedstoneTabContent() {
        this.addRenderableWidget(Button.builder(
                Component.literal("ON"),
                button -> sendMessage("Redstone activado")
        ).bounds(guiLeft + 20, guiTop + 40, 50, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("OFF"),
                button -> sendMessage("Redstone desactivado")
        ).bounds(guiLeft + 80, guiTop + 40, 50, 20).build());

        this.addRenderableWidget(new PowerSlider(
                guiLeft + 20, guiTop + 70, 150, 20,
                Component.literal("Potencia: "), 0.5
        ));
    }

    private void addInfoTabContent() {
        EditBox notesField = new EditBox(this.font,
                guiLeft + 20, guiTop + 40, 216, 100,
                Component.literal("Notas"));
        notesField.setMaxLength(500);
        notesField.setValue("Escribe tus notas aquí...");
        this.addRenderableWidget(notesField);
    }

    private void addTradeTabContent() {
        this.addRenderableWidget(Button.builder(
                Component.literal(" Comprar"),
                button -> sendMessage("Comprando...")
        ).bounds(guiLeft + 20, guiTop + 40, 80, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal(" Vender"),
                button -> sendMessage("Vendiendo...")
        ).bounds(guiLeft + 110, guiTop + 40, 80, 20).build());
    }

    private void sendMessage(String message) {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.sendSystemMessage(Component.literal(message));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        renderVanillaContainer(graphics);

        for (int i = 0; i < tabs.size() && i < tabIcons.length; i++) {
            TabButton tab = tabs.get(i);
            graphics.renderItem(tabIcons[i], tab.getX() + 4, tab.getY() + 6);
        }

        String[] tabTitles = {"⚔ Combate", "🔴 Redstone", "📖 Información", "💎 Comercio"};
        graphics.drawCenteredString(this.font,
                Component.literal(tabTitles[currentTab]),
                guiLeft + xSize / 2, guiTop + 15, 0x404040);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderVanillaContainer(GuiGraphics graphics) {
        graphics.fill(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, 0xFFC6C6C6);
        graphics.fill(guiLeft, guiTop, guiLeft + xSize - 1, guiTop + 1, 0xFFFFFFFF);
        graphics.fill(guiLeft, guiTop, guiLeft + 1, guiTop + ySize - 1, 0xFFFFFFFF);
        graphics.fill(guiLeft, guiTop + ySize - 1, guiLeft + xSize, guiTop + ySize, 0xFF555555);
        graphics.fill(guiLeft + xSize - 1, guiTop, guiLeft + xSize, guiTop + ySize, 0xFF555555);

        graphics.fill(guiLeft + 10, guiTop + 30, guiLeft + xSize - 10, guiTop + 31, 0xFF888888);
    }

    class TabButton extends Button {
        private boolean selected;

        public TabButton(int x, int y, int width, int height, Component message,
                         OnPress onPress, boolean selected) {
            super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
            this.selected = selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            if (selected) {
                graphics.fill(getX(), getY() + 2, getX() + width, getY() + height + 2, 0xFFC6C6C6);
                graphics.fill(getX(), getY() + 2, getX() + width - 1, getY() + 3, 0xFFFFFFFF);
                graphics.fill(getX(), getY() + 2, getX() + 1, getY() + height + 2, 0xFFFFFFFF);
                graphics.fill(getX() + width - 1, getY() + 2, getX() + width, getY() + height, 0xFF555555);
            } else {
                graphics.fill(getX(), getY() + 4, getX() + width - 2, getY() + height, 0xFFA0A0A0);
                graphics.fill(getX(), getY() + 4, getX() + width - 3, getY() + 5, 0xFFFFFFFF);
                graphics.fill(getX(), getY() + 4, getX() + 1, getY() + height - 1, 0xFFFFFFFF);
            }

            graphics.drawString(font, getMessage(),
                    getX() + 22, getY() + 10, selected ? 0x404040 : 0x808080, false);
        }
    }

    class PowerSlider extends net.minecraft.client.gui.components.AbstractSliderButton {
        public PowerSlider(int x, int y, int width, int height, Component prefix, double value) {
            super(x, y, width, height, prefix, value);
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            int power = (int)(this.value * 15);
            this.setMessage(Component.literal("Potencia: " + power));
        }

        @Override
        protected void applyValue() {
        }
    }
}
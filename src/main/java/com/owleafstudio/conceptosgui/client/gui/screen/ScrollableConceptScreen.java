package com.owleafstudio.conceptosgui.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ScrollableConceptScreen extends Screen {
    private ItemListWidget itemList;
    private Button selectButton;
    private Button deleteButton;
    private Button addButton;
    private int entryCounter = 0;

    public ScrollableConceptScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();

        this.itemList = new ItemListWidget(this.minecraft, this.width - 40, this.height,
                40, this.height - 60, 30);

        for (int i = 0; i < 20; i++) {
            addNewEntry();
        }
        this.addWidget(itemList);
        int buttonY = this.height - 50;
        int buttonWidth = 80;
        int centerX = this.width / 2;


        this.selectButton = Button.builder(
                Component.literal("Seleccionar"),
                button -> {
                    ItemListWidget.ItemEntry selected = itemList.getSelected();
                    if (selected != null) {
                        selected.onSelect();
                    }
                }
        ).bounds(centerX - 130, buttonY, buttonWidth, 20).build();
        this.addRenderableWidget(selectButton);

        this.addButton = Button.builder(
                Component.literal("Añadir"),
                button -> {
                    addNewEntry();
                    itemList.setScrollAmount(itemList.getMaxScroll());
                }
        ).bounds(centerX - 40, buttonY, buttonWidth, 20).build();
        this.addRenderableWidget(addButton);


        this.deleteButton = Button.builder(
                Component.literal("Eliminar"),
                button -> {
                    ItemListWidget.ItemEntry selected = itemList.getSelected();
                    if (selected != null) {
                        itemList.removeEntryFromList(selected);
                        sendMessage("Elemento eliminado");
                    }
                }
        ).bounds(centerX + 50, buttonY, buttonWidth, 20).build();
        this.addRenderableWidget(deleteButton);

        this.addRenderableWidget(Button.builder(
                Component.literal("Cerrar"),
                button -> this.onClose()
        ).bounds(centerX - 40, buttonY + 25, buttonWidth, 20).build());
    }

    private void addNewEntry() {
        ItemStack[] items = {
                new ItemStack(Items.DIAMOND),
                new ItemStack(Items.EMERALD),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.REDSTONE),
                new ItemStack(Items.LAPIS_LAZULI),
                new ItemStack(Items.COAL),
                new ItemStack(Items.QUARTZ)
        };

        ItemStack randomItem = items[entryCounter % items.length];
        String name = "Elemento #" + (++entryCounter) + " - " + randomItem.getHoverName().getString();

        itemList.addEntryToList(new ItemListWidget.ItemEntry(itemList, name, randomItem,
                () -> sendMessage("Seleccionado: " + name)));
    }

    private void sendMessage(String message) {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.sendSystemMessage(Component.literal(message));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        int listX = 20;
        int listWidth = this.width - 40;
        int listY = 40;
        int listHeight = this.height - 100;

        renderVanillaPanel(graphics, listX - 5, listY - 5, listWidth + 10, listHeight + 10);


        this.itemList.render(graphics, mouseX, mouseY, partialTick);


        graphics.drawCenteredString(this.font, this.title,
                this.width / 2, 15, 0xFFFFFF);


        ItemListWidget.ItemEntry selected = itemList.getSelected();
        if (selected != null) {
            graphics.drawCenteredString(this.font,
                    Component.literal("Seleccionado: " + selected.getName()).withStyle(s -> s.withColor(0x00FF00)),
                    this.width / 2, 25, 0xFFFFFF);
        }


        graphics.drawString(this.font,
                Component.literal("Total: " + itemList.children().size() + " elementos"),
                25, this.height - 35, 0xAAAAAA);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderVanillaPanel(GuiGraphics graphics, int x, int y, int width, int height) {

        graphics.fill(x, y, x + width, y + height, 0xFF000000);

        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF262626);

        graphics.fill(x + 1, y + 1, x + width - 1, y + 2, 0xFF000000);
        graphics.fill(x + 1, y + 1, x + 2, y + height - 1, 0xFF000000);
    }

    public static class ItemListWidget extends ObjectSelectionList<ItemListWidget.ItemEntry> {
        public ItemListWidget(Minecraft minecraft, int width, int height,
                              int top, int bottom, int itemHeight) {
            super(minecraft, width, height, top, bottom, itemHeight);
            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
        }

        public void addEntryToList(ItemEntry entry) {
            this.addEntry(entry);
        }

        public void removeEntryFromList(ItemEntry entry) {
            this.removeEntry(entry);
        }

        @Override
        protected void renderBackground(GuiGraphics graphics) {
        }

        public static class ItemEntry extends ObjectSelectionList.Entry<ItemEntry> {
            private final ItemListWidget parentList;
            private final String text;
            private final ItemStack icon;
            private final Runnable onSelect;

            public ItemEntry(ItemListWidget parentList, String text, ItemStack icon, Runnable onSelect) {
                this.parentList = parentList;
                this.text = text;
                this.icon = icon;
                this.onSelect = onSelect;
            }

            public String getName() {
                return text;
            }

            public void onSelect() {
                onSelect.run();
            }

            @Override
            public void render(GuiGraphics graphics, int index, int top, int left,
                               int width, int height, int mouseX, int mouseY,
                               boolean isMouseOver, float partialTick) {
                if (index % 2 == 0) {
                    graphics.fill(left, top, left + width, top + height, 0x20FFFFFF);
                }

                if (isMouseOver) {
                    graphics.fill(left, top, left + width, top + height, 0x40FFFFFF);
                }

                graphics.renderItem(icon, left + 5, top + (height - 16) / 2);

                graphics.drawString(parentList.minecraft.font, text,
                        left + 25, top + (height - 9) / 2, 0xFFFFFF);
                if (parentList != null && parentList.getSelected() == this) {
                    graphics.fill(left, top, left + 2, top + height, 0xFF00FF00);
                }
            }

            @Override
            public Component getNarration() {
                return Component.literal(text);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == 0) {
                    this.onSelect();
                    return true;
                }
                return false;
            }
        }
    }
}
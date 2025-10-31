package com.owleafstudio.conceptosgui.init;

import com.owleafstudio.conceptosgui.ConceptosGUI;
import com.owleafstudio.conceptosgui.item.GuiOpenerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ConceptosGUI.MODID);

    public static final RegistryObject<Item> GUI_OPENER_BASIC = ITEMS.register("gui_opener_basic",
            () -> new GuiOpenerItem(new Item.Properties(), GuiOpenerItem.GuiType.BASIC));

    public static final RegistryObject<Item> GUI_OPENER_INVENTORY = ITEMS.register("gui_opener_inventory",
            () -> new GuiOpenerItem(new Item.Properties(), GuiOpenerItem.GuiType.INVENTORY));

    public static final RegistryObject<Item> GUI_OPENER_CONFIG = ITEMS.register("gui_opener_config",
            () -> new GuiOpenerItem(new Item.Properties(), GuiOpenerItem.GuiType.CONFIG));

    public static final RegistryObject<Item> GUI_OPENER_SCROLLABLE = ITEMS.register("gui_opener_scrollable",
            () -> new GuiOpenerItem(new Item.Properties(), GuiOpenerItem.GuiType.SCROLLABLE));

    public static final RegistryObject<Item> GUI_OPENER_TABBED = ITEMS.register("gui_opener_tabbed",
            () -> new GuiOpenerItem(new Item.Properties(), GuiOpenerItem.GuiType.TABBED));

    public static final RegistryObject<Item> GUI_OPENER_ANIMATED = ITEMS.register("gui_opener_animated",
            () -> new GuiOpenerItem(new Item.Properties(), GuiOpenerItem.GuiType.ANIMATED));

    public static final RegistryObject<Item> GUI_OPENER_PROGRESS = ITEMS.register("gui_opener_progress",
            () -> new GuiOpenerItem(new Item.Properties(), GuiOpenerItem.GuiType.PROGRESS));

    // Block Item
    public static final RegistryObject<Item> GUI_BLOCK_ITEM = ITEMS.register("gui_block",
            () -> new BlockItem(ModBlocks.GUI_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
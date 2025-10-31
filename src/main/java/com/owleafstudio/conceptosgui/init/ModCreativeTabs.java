package com.owleafstudio.conceptosgui.init;

import com.owleafstudio.conceptosgui.ConceptosGUI;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ConceptosGUI.MODID);

    public static final RegistryObject<CreativeModeTab> CONCEPTOS_TAB =
            CREATIVE_MODE_TABS.register("conceptos_tab",
                    () -> CreativeModeTab.builder()
                            .icon(() -> new ItemStack(Items.COMPASS))
                            .title(Component.literal("Conceptos GUI"))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.GUI_OPENER_BASIC.get());
                                output.accept(ModItems.GUI_OPENER_INVENTORY.get());
                                output.accept(ModItems.GUI_OPENER_CONFIG.get());
                                output.accept(ModItems.GUI_OPENER_SCROLLABLE.get());
                                output.accept(ModItems.GUI_OPENER_TABBED.get());
                                output.accept(ModItems.GUI_OPENER_ANIMATED.get());
                                output.accept(ModItems.GUI_OPENER_PROGRESS.get());
                                output.accept(ModItems.GUI_BLOCK_ITEM.get());
                            })
                            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
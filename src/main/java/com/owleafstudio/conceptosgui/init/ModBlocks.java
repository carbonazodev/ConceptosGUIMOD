package com.owleafstudio.conceptosgui.init;

import com.owleafstudio.conceptosgui.ConceptosGUI;
import com.owleafstudio.conceptosgui.block.GuiBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ConceptosGUI.MODID);

    public static final RegistryObject<Block> GUI_BLOCK = BLOCKS.register("gui_block",
            () -> new GuiBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f)
                    .requiresCorrectToolForDrops()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
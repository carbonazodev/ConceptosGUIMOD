package com.owleafstudio.conceptosgui.init;

import com.owleafstudio.conceptosgui.ConceptosGUI;
import com.owleafstudio.conceptosgui.block.entity.GuiBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ConceptosGUI.MODID);

    public static final RegistryObject<BlockEntityType<GuiBlockEntity>> GUI_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("gui_block_entity",
                    () -> BlockEntityType.Builder.of(GuiBlockEntity::new,
                            ModBlocks.GUI_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
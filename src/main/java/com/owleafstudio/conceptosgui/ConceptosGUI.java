package com.owleafstudio.conceptosgui;

import com.mojang.logging.LogUtils;
import com.owleafstudio.conceptosgui.init.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ConceptosGUI.MODID)
public class ConceptosGUI {
    public static final String MODID = "conceptosgui";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ConceptosGUI() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("[ConceptosGUIMod] Inicializado, feeldev es gay!");
        LOGGER.info("[ConceptosGUIMod] Esto es un mod?");
    }
}
package com.owleafstudio.conceptosgui.item;

import com.owleafstudio.conceptosgui.client.gui.screen.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuiOpenerItem extends Item {
    private final GuiType guiType;

    public GuiOpenerItem(Properties properties, GuiType guiType) {
        super(properties.stacksTo(1));
        this.guiType = guiType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            openGui(player);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @OnlyIn(Dist.CLIENT)
    private void openGui(Player player) {
        Minecraft mc = Minecraft.getInstance();
        switch (guiType) {
            case BASIC -> mc.setScreen(new BasicConceptScreen(Component.literal("GUI Básica")));
            case INVENTORY -> player.sendSystemMessage(Component.literal("Usa el bloque GUI para inventarios"));
            case CONFIG -> mc.setScreen(new ConfigConceptScreen(Component.literal("Configuración")));
            case SCROLLABLE -> mc.setScreen(new ScrollableConceptScreen(Component.literal("Lista Scrolleable")));
            case TABBED -> mc.setScreen(new TabbedConceptScreen(Component.literal("GUI con Pestañas")));
            case ANIMATED -> mc.setScreen(new AnimatedConceptScreen(Component.literal("GUI Animada")));
            case PROGRESS -> mc.setScreen(new ProgressConceptScreen(Component.literal("Barras de Progreso")));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Click derecho para abrir"));
        switch (guiType) {
            case BASIC -> tooltip.add(Component.literal("§eGUI con widgets básicos"));
            case INVENTORY -> tooltip.add(Component.literal("§eGUI con inventario"));
            case CONFIG -> tooltip.add(Component.literal("§eGUI de configuración"));
            case SCROLLABLE -> tooltip.add(Component.literal("§eLista scrolleable"));
            case TABBED -> tooltip.add(Component.literal("§eGUI con pestañas"));
            case ANIMATED -> tooltip.add(Component.literal("§eGUI con animaciones"));
            case PROGRESS -> tooltip.add(Component.literal("§eBarras de progreso"));
        }
    }

    public enum GuiType {
        BASIC, INVENTORY, CONFIG, SCROLLABLE, TABBED, ANIMATED, PROGRESS
    }
}
package xyz.starmun.justenoughkeys.forge.events.handlers;

import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xyz.starmun.justenoughkeys.common.JEKControlScreen;
import xyz.starmun.justenoughkeys.common.contracts.IJEKOptionsSubScreenExtensions;

public class ClientEventHandler {
    @SubscribeEvent
    public void openGui(GuiOpenEvent event) {
        try {
            if(event.getGui() instanceof ControlsScreen && !(event.getGui() instanceof JEKControlScreen)) {
                IJEKOptionsSubScreenExtensions optionsSubScreen = (IJEKOptionsSubScreenExtensions) event.getGui();
                event.setGui(new JEKControlScreen(optionsSubScreen.jek$getLastScreen(), Minecraft.getInstance().options));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

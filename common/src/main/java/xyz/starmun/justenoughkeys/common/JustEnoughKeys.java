package xyz.starmun.justenoughkeys.common;


import me.shedaniel.architectury.event.events.GuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.starmun.justenoughkeys.common.gui.JEKControlScreen;

public class JustEnoughKeys {
    public static final String MOD_ID = "justenoughkeys";
    public static final String MOD_NAME = "Just Enough Keys";
    // We can use this if we don't want to use DeferredRegister
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static void init() {
        LOGGER.info("Loading Key Bindings");
        GuiEvent.SET_SCREEN.register(JustEnoughKeys::setScreen);
    }

    private static InteractionResultHolder<Screen> setScreen(Screen screen) {
            try {
                if(screen instanceof ControlsScreen && !(screen instanceof JEKControlScreen)) {
                    JEKControlScreen controlsScreen = new JEKControlScreen(Minecraft.getInstance().screen, Minecraft.getInstance().options);

                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, controlsScreen);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, screen);
    }
}

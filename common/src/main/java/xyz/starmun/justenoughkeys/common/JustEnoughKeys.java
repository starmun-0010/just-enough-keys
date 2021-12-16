package xyz.starmun.justenoughkeys.common;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import xyz.starmun.justenoughkeys.common.client.JEKKeyMapping;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

public class JustEnoughKeys {
    public static final String MOD_ID = "justenoughkeys";
    public static final String MOD_NAME = "Just Enough Keys";
    public static KeyMapping dropStack;
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static void init() {
        LOGGER.info("Loading Key Bindings");
    }
    static {
        dropStack = new JEKKeyMapping("key.justenoughkeys.dropstack",
                new ModifierKeyMap(){{
                    set(ModifierKey.KEYBOARD_LEFT_CONTROL,true);
                }},
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Q,"key.categories.inventory");
    }
}

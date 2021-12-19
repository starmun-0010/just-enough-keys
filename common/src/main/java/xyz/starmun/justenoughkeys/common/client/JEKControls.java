package xyz.starmun.justenoughkeys.common.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

public class JEKControls {
    public static KeyMapping dropStack;

    static {
        dropStack = new JEKKeyMapping("key.justenoughkeys.dropstack",
                new ModifierKeyMap(){{
                    set(ModifierKey.KEYBOARD_LEFT_CONTROL,true);
                }},
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Q,"key.categories.inventory");
    }
}

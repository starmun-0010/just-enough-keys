package xyz.starmun.justenoughkeys.common.data;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class ModifierKey {
    public final int id;
    public final String name;
    final int value;

    static final HashMap<Integer, ModifierKey> MODIFIER_KEYS = new HashMap<>();

    public static final ModifierKey UNKNOWN = new ModifierKey(1, "UNKNOWN", GLFW.GLFW_KEY_UNKNOWN);
    public static final ModifierKey KEYBOARD_LEFT_ALT = new ModifierKey(105, "LEFT ALT", GLFW.GLFW_KEY_LEFT_ALT);
    public static final ModifierKey KEYBOARD_LEFT_CONTROL = new ModifierKey(106, "LEFT CTRL", GLFW.GLFW_KEY_LEFT_CONTROL);
    public static final ModifierKey KEYBOARD_LEFT_SHIFT = new ModifierKey(107, "LEFT SHIFT", GLFW.GLFW_KEY_LEFT_SHIFT);
    public static final ModifierKey KEYBOARD_RIGHT_ALT = new ModifierKey(109, "RIGHT ALT", GLFW.GLFW_KEY_RIGHT_ALT);
    public static final ModifierKey KEYBOARD_RIGHT_CONTROL = new ModifierKey(110, "RIGHT CTRL", GLFW.GLFW_KEY_RIGHT_CONTROL);
    public static final ModifierKey KEYBOARD_RIGHT_SHIFT = new ModifierKey(111, "RIGHT SHIFT", GLFW.GLFW_KEY_RIGHT_SHIFT);

    private ModifierKey(int id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    static {
        Field[] fieldsIncludingSuperclasses = ModifierKey.class.getDeclaredFields();
        for (Field field : fieldsIncludingSuperclasses) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType() == ModifierKey.class) {
                try {
                    ModifierKey key = (ModifierKey) field.get(null);
                    MODIFIER_KEYS.put(key.value, key);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isModifierKey() {
        return ModifierKey.isModifierKey(this.value);
    }

    public static boolean isModifierKey(InputConstants.Key key) {
        int keyValue = key.getValue();
        return ModifierKey.isModifierKey(keyValue);
    }
    private static boolean isModifierKey(int value) {
     return value != UNKNOWN.value && MODIFIER_KEYS.containsKey(value);
    }
    public static ModifierKey modifierKeyFromValue(int value) {
        if(!MODIFIER_KEYS.containsKey(value))return UNKNOWN;
        return MODIFIER_KEYS.get(value);
    }

    public String getName(){
        return name;
    }

}

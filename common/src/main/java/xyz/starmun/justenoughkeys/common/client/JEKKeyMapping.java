package xyz.starmun.justenoughkeys.common.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

public class JEKKeyMapping extends KeyMapping {

    public JEKKeyMapping(String name, ModifierKeyMap modifierKeyMap, InputConstants.Type type, int keyCode, String category) {
        super(name, type, keyCode, category);
        ((IJEKKeyMappingExtensions) this).setDefaultModifierKeyMap(modifierKeyMap);
    }
}

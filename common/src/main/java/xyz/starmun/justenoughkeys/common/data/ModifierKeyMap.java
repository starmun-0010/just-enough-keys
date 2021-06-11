package xyz.starmun.justenoughkeys.common.data;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

import java.util.HashMap;

public class ModifierKeyMap extends HashMap<Integer, ModifierKey> {

    public void remove(KeyMapping key){
        int keyValue = ((IJEKKeyMappingExtensions)key).jek$getKey().getValue();
        this.remove(keyValue);
    }
    public ModifierKey put(int id, ModifierKey key){
        if(key== ModifierKey.UNKNOWN)
            return null;
        return super.put(id, key);
    }
    public ModifierKey put(InputConstants.Key key){
        if(ModifierKey.isModifierKey(key)){
            ModifierKey modifierKey = ModifierKey.modifierKeyFromValue(key.getValue());
            return super.put(modifierKey.id, modifierKey);
        }
        return null;
    }
}

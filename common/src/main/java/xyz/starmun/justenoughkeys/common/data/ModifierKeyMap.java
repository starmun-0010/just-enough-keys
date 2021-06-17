package xyz.starmun.justenoughkeys.common.data;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

import java.util.BitSet;
import java.util.HashMap;

public class ModifierKeyMap extends HashMap<Integer, ModifierKey> {

    private final BitSet bitSet = new BitSet();

    public void set(ModifierKey key, boolean isPressed){
        if(key== ModifierKey.UNKNOWN)
            return;
        bitSet.set(key.id,isPressed);
        if(isPressed){
            super.put(key.id, key);

        }
        else {
            super.remove(key.id);
        }
    }
    public void set(InputConstants.Key key, boolean isPressed){
        if(ModifierKey.isModifierKey(key)){
            ModifierKey modifierKey = ModifierKey.modifierKeyFromValue(key.getValue());
            this.set(modifierKey, isPressed);
        }
    }
    public boolean any(){
        return !this.bitSet.isEmpty();
    }
    public void clear(KeyMapping keyMapping){
        set(ModifierKey.modifierKeyFromValue(((IJEKKeyMappingExtensions)keyMapping).jek$getKey().getValue()),false);
    }
    public void clear(){
        bitSet.clear();
        super.clear();
    }
    public boolean isPressed(){
        return IJEKKeyMappingExtensions.CURRENT_PRESSED_MODIFIERS.bitSet.equals(this.bitSet);
    }
}

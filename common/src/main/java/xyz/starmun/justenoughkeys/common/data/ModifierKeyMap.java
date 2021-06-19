package xyz.starmun.justenoughkeys.common.data;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

import java.util.BitSet;
import java.util.HashMap;

public class ModifierKeyMap extends HashMap<Integer, ModifierKey> {

    private final BitSet bitSet = new BitSet();

    public ModifierKey set(ModifierKey key, boolean isPressed){
        if(key== ModifierKey.UNKNOWN)
            return ModifierKey.UNKNOWN;
        bitSet.set(key.id,isPressed);
        if(isPressed){
            return super.put(key.id, key);

        }
        else {
            return super.remove(key.id);
        }
    }
    public ModifierKey set(InputConstants.Key key, boolean isPressed){
        if(ModifierKey.isModifierKey(key)){
            ModifierKey modifierKey = ModifierKey.modifierKeyFromValue(key.getValue());
            return this.set(modifierKey, isPressed);
        }
        return ModifierKey.UNKNOWN;
    }
    public ModifierKey set(InputConstants.Key key){
      return set(key,true);
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

package xyz.starmun.justenoughkeys.common.data;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.system.CallbackI;
import xyz.starmun.justenoughkeys.common.client.JEKKeyMapping;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

import java.util.BitSet;
import java.util.HashMap;

public class ModifierKeyMap extends HashMap<Integer, ModifierKey> {

    private BitSet bitMap = new BitSet();

    public ModifierKey set(ModifierKey key, boolean isPressed){
        if(key== ModifierKey.UNKNOWN)
            return null;
        bitMap.set(key.id,isPressed);
        if(isPressed){
            return super.put(key.id, key);

        }
        else {
            return super.remove(key.id);
        }
    }
    public ModifierKey set(InputConstants.Key key,boolean isPressed){
        if(ModifierKey.isModifierKey(key)){
            ModifierKey modifierKey = ModifierKey.modifierKeyFromValue(key.getValue());
            return this.set(modifierKey, isPressed);
        }
        return null;
    }
    public boolean any(){
        return !this.bitMap.isEmpty();
    }
    public void clear(KeyMapping keyMapping){
        set(ModifierKey.modifierKeyFromValue(((IJEKKeyMappingExtensions)keyMapping).jek$getKey().getValue()),false);
    }
    public void clear(){
        bitMap.clear();
        super.clear();
    }
    public boolean isPressed(){
        return JEKKeyMapping.CURRENT_PRESSED_MODIFIERS.bitMap.equals(this.bitMap);
    }
}

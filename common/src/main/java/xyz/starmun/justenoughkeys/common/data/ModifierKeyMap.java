package xyz.starmun.justenoughkeys.common.data;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Locale;

public class ModifierKeyMap extends HashMap<Integer, ModifierKey> {

    private final BitSet bitSet = new BitSet();

    public void set(ModifierKeyMap modifierKeyMap){
        this.clear();
        modifierKeyMap.forEach((integer, modifierKey) -> {
            if(modifierKey== ModifierKey.UNKNOWN){
                return;
            }
            bitSet.set(modifierKey.id, true);
            super.put(modifierKey.id, modifierKey);
        });
    }
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
    public void setAll(){
        ModifierKey.MODIFIER_KEYS.values().forEach(modifierKey -> {
            if (modifierKey == ModifierKey.UNKNOWN) {
                return;
            }
            set(ModifierKey.modifierKeyFromValue(modifierKey.value),
                    InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), modifierKey.value));
        });
    }

    public boolean search(String allParametersStrippedQuery) {
        return this.values().stream()
                .anyMatch(modifierKey -> modifierKey.getDisplayName().toLowerCase(Locale.ROOT)
                        .startsWith(allParametersStrippedQuery)
                        || Arrays.stream(modifierKey.getDisplayName().toLowerCase(Locale.ROOT)
                        .split(" ")).anyMatch(substring->substring.startsWith(allParametersStrippedQuery)));
    }
}

package xyz.starmun.justenoughkeys.contracts;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import xyz.starmun.justenoughkeys.data.ModifierKey;
import xyz.starmun.justenoughkeys.data.ModifierKeyMap;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface IJEKKeyMappingExtensions {

    InputConstants.Key jek$getKey();

    void jek$setClickCount(int i);

    int jek$getClickCount();
    default ModifierKey getPlatformDefaultModifierKey(){
        return ModifierKey.UNKNOWN;
    }
    default void setPlatformDefaultModifierKey(ModifierKey modifierKey){
        return ;
    }
    Map<String, KeyMapping> ALL = Maps.newHashMap();
    Map<InputConstants.Key, ConcurrentLinkedQueue<KeyMapping>> MAP = Maps.newHashMap();
    ModifierKeyMap CURRENT_PRESSED_MODIFIERS = new ModifierKeyMap();

    ModifierKeyMap jek$getModifierKeyMap();

    static void initMAP(KeyMapping keyMapping) {
        InputConstants.Key key = ((IJEKKeyMappingExtensions) (keyMapping)).jek$getKey();
        if (!MAP.containsKey(key)) {
            MAP.put(key, new ConcurrentLinkedQueue<>(Collections.singleton(keyMapping)));
        } else {
            MAP.get(key).add(keyMapping);
        }
    }

    static void click(InputConstants.Key key) {
        getMatchingKeyMappings(key).forEach(keyMapping ->
                ((IJEKKeyMappingExtensions) keyMapping).jek$setClickCount(((IJEKKeyMappingExtensions) keyMapping).jek$getClickCount() + 1));
    }

    static void set(InputConstants.Key key, boolean pressed) {
        CURRENT_PRESSED_MODIFIERS.set(ModifierKey.modifierKeyFromValue(key.getValue()), pressed);
        if (!pressed) {
            Queue<KeyMapping> keyMappings = MAP.get(key);
            if (keyMappings != null) {
                keyMappings.forEach(keyMapping -> keyMapping.setDown(false));
            }
        }
        getMatchingKeyMappings(key).forEach(keyMapping -> keyMapping.setDown(pressed));
    }

    static void setAll() {
        CURRENT_PRESSED_MODIFIERS.setAll();
        MAP.values().stream().flatMap(Collection::stream).collect(Collectors.toSet())
                .forEach(keyMapping -> keyMapping.setDown(!keyMapping.isUnbound()
                        && ((IJEKKeyMappingExtensions) keyMapping).jek$getKey().getType() == InputConstants.Type.KEYSYM
                        && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(),
                        ((IJEKKeyMappingExtensions) keyMapping).jek$getKey().getValue())));
    }

    static void releaseAll() {
        CURRENT_PRESSED_MODIFIERS.clear();
        MAP.values().stream().flatMap(Collection::stream).collect(Collectors.toList()).forEach((jekKeyMappings) -> jekKeyMappings.setDown(false));
    }

    static void resetMapping() {
        KeyMapping.resetMapping();
        MAP.clear();
        ALL.values().forEach(IJEKKeyMappingExtensions::initMAP);
    }

    static Stream<KeyMapping> getMatchingKeyMappings(InputConstants.Key key) {
        Queue<KeyMapping> candidateKeys = MAP.get(key);
        if (candidateKeys == null) return Stream.empty();
        Set<KeyMapping> keyMappings = candidateKeys.stream().filter(keyMapping -> ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().isPressed()).collect(Collectors.toSet());
        if (keyMappings.isEmpty()) {
            return MAP.get(key).stream().filter(keyMapping -> !((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().any());
        } else {
            return keyMappings.stream();
        }
    }
}

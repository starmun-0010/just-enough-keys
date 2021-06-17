package xyz.starmun.justenoughkeys.common.contracts;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface IJEKKeyMapping {
     Map<String, KeyMapping> ALL = Maps.newHashMap();
     Map<InputConstants.Key, ConcurrentLinkedQueue<KeyMapping>> MAP = Maps.newHashMap();
     ModifierKeyMap CURRENT_PRESSED_MODIFIERS = new ModifierKeyMap();


     ModifierKeyMap getModifierKeyMap();


     static void initMAP(KeyMapping keyMapping) {
        InputConstants.Key key = ((IJEKKeyMappingExtensions) (keyMapping)).jek$getKey();
        if (!MAP.containsKey(key)) {
            MAP.put(key, new ConcurrentLinkedQueue<>(Collections.singleton(keyMapping)));
        } else {
            MAP.get(key).add(keyMapping);
        }
    }



     static void click(InputConstants.Key key) {
        preventVanillaClick(key);
        getMatchingKeyMappings(key).forEach(keyMapping ->
                ((IJEKKeyMappingExtensions) keyMapping).jek$setClickCount(((IJEKKeyMappingExtensions) keyMapping).jek$getClickCount() + 1));
    }

     static void preventVanillaClick(InputConstants.Key key) {
        if (!MAP.containsKey(key)) {
            return;
        }
        MAP.get(key).forEach(keyMapping -> {
            int clickCount = ((IJEKKeyMappingExtensions) keyMapping).jek$getClickCount();
            ((IJEKKeyMappingExtensions) keyMapping).jek$setClickCount(clickCount == 0 ? 0 : clickCount - 1);
        });
    }

     static void set(InputConstants.Key key, boolean pressed) {
        preventVanillaSet(key);
        getMatchingKeyMappings(key).forEach(keyMapping -> keyMapping.setDown(pressed));
    }

     static void preventVanillaSet(InputConstants.Key key) {
        if (!MAP.containsKey(key)) {
            return;
        }
        MAP.get(key).forEach(keyMapping -> {
            keyMapping.setDown(false);
        });
    }

     static void setAll() {
        MAP.values().stream().flatMap(Collection::stream).collect(Collectors.toSet())
                .forEach(keyMapping -> keyMapping.setDown(!keyMapping.isUnbound()
                        && ((IJEKKeyMappingExtensions) keyMapping).jek$getKey().getType() == InputConstants.Type.KEYSYM
                        && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(),
                        ((IJEKKeyMappingExtensions) keyMapping).jek$getKey().getValue())));
    }

     static void releaseAll() {
        MAP.values().stream().flatMap(Collection::stream).collect(Collectors.toList()).forEach((jekKeyMappings) -> jekKeyMappings.setDown(false));
    }

     static void resetMapping() {
        KeyMapping.resetMapping();
        MAP.clear();
        ALL.values().forEach(keyMapping -> initMAP(keyMapping));
    }

     static Stream<KeyMapping> getMatchingKeyMappings(InputConstants.Key key) {
        Queue<KeyMapping> candidateKeys = MAP.get(key);
        if (candidateKeys == null) return Stream.empty();
        Set<KeyMapping> keyMappings = candidateKeys.stream().filter(keyMapping -> ((IJEKKeyMapping)keyMapping).getModifierKeyMap().isPressed()).collect(Collectors.toSet());
        if (keyMappings.isEmpty()) {
            return MAP.get(key).stream().filter(keyMapping -> !((IJEKKeyMapping)keyMapping).getModifierKeyMap().any());
        } else {
            return keyMappings.stream();
        }
    }
}

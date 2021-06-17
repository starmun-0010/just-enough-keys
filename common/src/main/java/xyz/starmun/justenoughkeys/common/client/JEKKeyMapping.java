package xyz.starmun.justenoughkeys.common.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMapping;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JEKKeyMapping extends KeyMapping implements IJEKKeyMapping {

    ModifierKeyMap modifierKeyMap = new ModifierKeyMap();

    public JEKKeyMapping(String name, int i, String category) {
        this(name, InputConstants.Type.KEYSYM, i, category);

    }

    public JEKKeyMapping(String name, InputConstants.Type type, int i, String category) {
        super(name, type, i, category);
        ALL.put(name, this);
        initMAP(this);
    }

    private static void initMAP(JEKKeyMapping keyMapping) {
      IJEKKeyMapping.initMAP(keyMapping);
    }

    @Override
    public Component getTranslatedKeyMessage() {
        StringBuilder builder = new StringBuilder();
        this.getModifierKeyMap().forEach((id, modifierKey) -> builder.append(modifierKey.name));
        return new TextComponent(builder.toString()).append(((IJEKKeyMappingExtensions) this).jek$getKey().getDisplayName());
    }

    @Override
    public boolean matches(int i, int j) {
        if (modifierKeyMap.any() && !modifierKeyMap.isPressed()) return false;
        return super.matches(i, j);
    }

    @Override
    public boolean matchesMouse(int i) {
        if (modifierKeyMap.any() && !modifierKeyMap.isPressed()) return false;
        return super.matchesMouse(i);
    }

    @Override
    public ModifierKeyMap getModifierKeyMap() {
       return modifierKeyMap;
    }

    public static void releaseAll() {
        IJEKKeyMapping.releaseAll();
    }

    public static void resetMapping() {
        IJEKKeyMapping.resetMapping();
    }
}

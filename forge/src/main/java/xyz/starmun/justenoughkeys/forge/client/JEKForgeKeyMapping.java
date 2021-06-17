package xyz.starmun.justenoughkeys.forge.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMapping;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

public class JEKForgeKeyMapping extends KeyMapping implements IJEKKeyMapping {
    public JEKForgeKeyMapping(String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier, InputConstants.Key keyCode, String category) {
        super(description,keyConflictContext,keyModifier,keyCode,category);
        ALL.put(description, this);
        IJEKKeyMapping.initMAP(this);
    }
    private ModifierKeyMap modifierKeyMap = new ModifierKeyMap();

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
}

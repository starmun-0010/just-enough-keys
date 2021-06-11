package xyz.starmun.justenoughkeys.common;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import org.lwjgl.glfw.GLFW;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

@Environment(EnvType.CLIENT)
public class JEKControlScreen extends ControlsScreen {

    public JEKControlScreen(Screen screen, Options options) {
        super(screen, options);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean keyPressed(int keyValue, int scanCode, int modifiers) {
        JustEnoughKeys.LOGGER.info("key hit");
        if (selectedKey == null) {
            return super.keyPressed(keyValue, scanCode, modifiers);
        }

        JustEnoughKeys.LOGGER.info((selectedKey).getName());
        if (keyValue == GLFW.GLFW_KEY_ESCAPE) {
            ((IJEKKeyMappingExtensions) selectedKey).jek$getModifierKeyMap().clear();
        }
        else if(selectedKey.isUnbound()){
            options.setKey(selectedKey, InputConstants.getKey(keyValue, scanCode));
        }
        else {
            int currentModifierKeyValue = ((IJEKKeyMappingExtensions) selectedKey).jek$getKey().getValue();
            ModifierKey currentModifierKey = ModifierKey.modifierKeyFromValue(currentModifierKeyValue);
            ModifierKey currentKey = ModifierKey.modifierKeyFromValue(keyValue);
            if (currentModifierKey != ModifierKey.UNKNOWN && currentKey == ModifierKey.UNKNOWN) {
                 ((IJEKKeyMappingExtensions) selectedKey).jek$getModifierKeyMap().put(currentModifierKey.id, currentModifierKey);
                options.setKey(selectedKey, InputConstants.getKey(keyValue, scanCode));

                JustEnoughKeys.LOGGER.info(currentModifierKey.name);
                JustEnoughKeys.LOGGER.info(currentKey.name);
                JustEnoughKeys.LOGGER.info(((IJEKKeyMappingExtensions) selectedKey).jek$getKey().getName());
                JustEnoughKeys.LOGGER.info(InputConstants.getKey(keyValue, scanCode).getDisplayName().getString());
            } else {
                ((IJEKKeyMappingExtensions) selectedKey).jek$getModifierKeyMap().put(currentKey.id, currentKey);
                JustEnoughKeys.LOGGER.info(currentKey.name);
                ((IJEKKeyMappingExtensions) selectedKey).jek$getModifierKeyMap().remove(selectedKey);
            }
        }
        lastKeySelection = Util.getMillis();
        KeyMapping.resetMapping();
        return true;

    }

    @Override
    public boolean keyReleased(int keyValue, int scanCode, int modifiers) {
        this.selectedKey = null;

        this.lastKeySelection = Util.getMillis();
        return super.keyReleased(keyValue, scanCode, modifiers);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(selectedKey == null){
            return super.mouseClicked(mouseX, mouseY, button);
        }
        InputConstants.Key key = ((IJEKKeyMappingExtensions) selectedKey).jek$getKey();
        ModifierKeyMap modifierKeyMap = ((IJEKKeyMappingExtensions) selectedKey).jek$getModifierKeyMap();
        modifierKeyMap.put(key);
        return super.mouseClicked(mouseX, mouseY, button);
    }
}

package xyz.starmun.justenoughkeys.common.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.MouseSettingsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import org.lwjgl.glfw.GLFW;
import xyz.starmun.justenoughkeys.common.contracts.IJEKControlScreenExtensions;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

import java.util.Arrays;

public class JEKControlScreen extends ControlsScreen {

    private Button resetButton;

    public JEKControlScreen(Screen screen, Options options ) {
        super(screen, options);
    }

    protected void init() {
        ((IJEKControlScreenExtensions)this).jek$setControlList(new JEKControlList(this, Minecraft.getInstance()));
        this.children.add(((IJEKControlScreenExtensions)this).jek$getControlList());
        this.setFocused(((IJEKControlScreenExtensions)this).jek$getControlList());
        initButtons();
    }

    @Override
    public boolean keyPressed(int keyValue, int scanCode, int modifiers) {
        if (selectedKey == null) {
            return super.keyPressed(keyValue, scanCode, modifiers);
        }
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
                ((IJEKKeyMappingExtensions) selectedKey).jek$getModifierKeyMap().set(currentModifierKey, true);
                options.setKey(selectedKey, InputConstants.getKey(keyValue, scanCode));
            } else {
                ((IJEKKeyMappingExtensions) selectedKey).jek$getModifierKeyMap().set(currentKey, true);
                ((IJEKKeyMappingExtensions) selectedKey).jek$getModifierKeyMap().clear(selectedKey);
            }
        }
        lastKeySelection = Util.getMillis();
        IJEKKeyMappingExtensions.resetMapping();
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
        modifierKeyMap.set(key, true);
        boolean result = super.mouseClicked(mouseX, mouseY, button);
        IJEKKeyMappingExtensions.resetMapping();
        return result;
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        this.renderBackground(poseStack);
        ((IJEKControlScreenExtensions)this).jek$getControlList().render(poseStack, i, j, f);
        drawCenteredString(poseStack, this.font, this.title.getString(), this.width/2, 8,  16777215);
        for (AbstractWidget button : buttons) {
            button.render(poseStack, i, j, f);
        }
        this.resetButton.active = Arrays.stream(this.options.keyMappings).anyMatch(keyMapping -> !keyMapping.isDefault());
    }

    //Mostly copied over from the parent class
    private void initButtons(){
        this.addButton(new Button(this.width / 2 - 155, 18, 150, 20, new TranslatableComponent("options.mouse_settings"), (button) -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(new MouseSettingsScreen(this, this.options));
        }));
        this.addButton(Option.AUTO_JUMP.createButton(this.options, this.width / 2 - 155 + 160, 18, 150));
        this.resetButton = this.addButton(new Button(this.width / 2 - 155, this.height - 29, 150, 20, new TranslatableComponent("controls.resetAll"), (button) -> {
            for (KeyMapping keyMapping: this.options.keyMappings){
                keyMapping.setKey(keyMapping.getDefaultKey());
            }
            IJEKKeyMappingExtensions.resetMapping();
        }));
        this.addButton(new Button(this.width / 2 - 155 + 160, this.height - 29, 150, 20, CommonComponents.GUI_DONE, (button) -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(this.lastScreen);
        }));
    }
}

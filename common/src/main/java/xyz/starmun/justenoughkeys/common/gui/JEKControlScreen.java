package xyz.starmun.justenoughkeys.common.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.MouseSettingsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.*;

import org.lwjgl.glfw.GLFW;
import xyz.starmun.justenoughkeys.common.contracts.IJEKControlScreenExtensions;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.contracts.IJEKScreenExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;
import xyz.starmun.justenoughkeys.common.data.ModifierKeyMap;

import java.util.*;

@Environment(EnvType.CLIENT)
public class JEKControlScreen extends KeyBindsScreen {
    private Button resetButton;
    private EditBox search;
    private String searchQuery = "";
    private List<Component> toolTipComponent;

    public JEKControlScreen(Screen screen, Options options) {
        super(screen, options);
    }

    protected void init() {
        KeyBindsList controlList = new JEKControlList(this, Minecraft.getInstance());
        ((IJEKControlScreenExtensions) this).jek$setControlList(controlList);

        this.toolTipComponent= new ArrayList<>();
        this.addWidget(controlList);

        initWidgets();
    }

    @Override
    public boolean keyPressed(int keyValue, int scanCode, int modifiers) {
        if (keyValue == GLFW.GLFW_KEY_ESCAPE && search.isFocused()) {
            search.setFocused(false);

            return true;
        }

        if (selectedKey == null) {
            if (!search.isFocused() && hasControlDown()
                    && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_F)) {
                search.setFocused(true);

                return true;
            }

            return search.keyPressed(keyValue, scanCode, modifiers) || super.keyPressed(keyValue, scanCode, modifiers);
        }

        if (keyValue == GLFW.GLFW_KEY_ESCAPE) {
            ((IJEKKeyMappingExtensions) selectedKey).jek$getModifierKeyMap().clear();
        } else {
            InputConstants.Key currentlyPressedKey = InputConstants.getKey(keyValue, scanCode);

            if (ModifierKey.isModifierKey(currentlyPressedKey)) {
                ((IJEKKeyMappingExtensions) selectedKey).jek$getModifierKeyMap().set(ModifierKey.modifierKeyFromValue(keyValue), true);
            }

            options.setKey(selectedKey, currentlyPressedKey);
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
        if (search.isMouseOver(mouseX, mouseY) && button == 1) {
            search.setValue("");
        } else if (search.mouseClicked(mouseX, mouseY, button)) {
            super.mouseClicked(mouseX, mouseY, button);
            return true;
        }

        if (selectedKey == null) {
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
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.renderBackground(guiGraphics);

        ((IJEKControlScreenExtensions) this).jek$getControlList().render(guiGraphics, i, j, f);
        guiGraphics.drawCenteredString(this.font, this.title.getString(), this.width / 2, 8, 16777215);

        for (Renderable renderable : ((IJEKScreenExtensions)this).jek$getRenderables()) {
            renderable.render(guiGraphics, i, j, f);
        }

        this.resetButton.active = Arrays.stream(this.options.keyMappings).anyMatch(keyMapping -> !keyMapping.isDefault());

        guiGraphics.drawCenteredString(this.font, Component.translatable("jek.controls.search.help.label"),this.width / 2, this.height - 40,16777215);
        this.search.render(guiGraphics, i, j, f);
        if (search.isMouseOver(i, j)) {
            guiGraphics.renderTooltip(this.font, this.toolTipComponent, Optional.empty(), i, j);
        }
        guiGraphics.drawString(this.font, Component.translatable("jek.controls.search.label"), this.width / 2 - 153, this.height - 60, 16777215);
    }

    // Mostly copied over from the parent class
    private void initWidgets() {
        int labelWidth = Minecraft.getInstance().font.width(Component.translatable("jek.controls.search.label"));
        this.search = new EditBox(this.font, this.width / 2 - 153 + labelWidth + 5, this.height - 65, 300 - labelWidth, 18, Component.empty());
        this.search.setMaxLength(200);
        this.addWidget(search);
        this.setInitialFocus(this.search);

        this.addRenderableWidget(Button.builder(Component.translatable("options.mouse_settings"), button -> {
                    assert this.minecraft != null;
                    this.minecraft.setScreen(new MouseSettingsScreen(this, this.options));
                })
                .bounds(this.width / 2 - 155, 18, 150, 20)
                .build());

        this.resetButton = this.addRenderableWidget(Button.builder(Component.translatable("controls.resetAll"), button -> {
                    for (KeyMapping keyMapping : this.options.keyMappings) {
                        keyMapping.setKey(keyMapping.getDefaultKey());
                        ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().clear();
                        ((IJEKKeyMappingExtensions) keyMapping).jek$getModifierKeyMap().set(((IJEKKeyMappingExtensions) keyMapping).jek$getDefaultModifierKeyMap());
                    }
                    IJEKKeyMappingExtensions.resetMapping();
                })
                .bounds(this.width / 2 - 155, this.height - 29, 150, 20)
                .build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
                    assert this.minecraft != null;
                    this.minecraft.setScreen(this.lastScreen);
                })
                .bounds(this.width / 2 - 155 + 160, this.height - 29, 150, 20)
                .build());

        this.toolTipComponent.add(Component.translatable("jek.controls.search.tooltip.title").withStyle(style -> style.withBold(true).withColor(ChatFormatting.DARK_AQUA)));
        this.toolTipComponent.add(Component.translatable("jek.controls.search.tooltip.description"));
        this.toolTipComponent.add(Component.translatable("jek.controls.search.tooltip.advanced.title").withStyle(ChatFormatting.GRAY));
        this.toolTipComponent.add(Component.translatable("jek.controls.search.tooltip.advanced.description"));
        this.toolTipComponent.add(Component.translatable("jek.controls.search.tooltip.advanced.filters.title").withStyle(ChatFormatting.DARK_GRAY));
        this.toolTipComponent.add(Component.translatable("jek.controls.search.tooltip.advanced.filters.description"));
        this.toolTipComponent.add(Component.translatable("jek.controls.search.tooltip.advanced.searchby.title").withStyle(ChatFormatting.DARK_GRAY));
        this.toolTipComponent.add(Component.translatable("jek.controls.search.tooltip.advanced.searchby.description"));
        this.toolTipComponent.add(Component.translatable("jek.controls.search.tooltip.advanced.example.prefix")
                .withStyle(ChatFormatting.DARK_GRAY)
                .append(Component.translatable("jek.controls.search.tooltip.advanced.example.query")
                .withStyle(ChatFormatting.GOLD))
                .append(Component.translatable("jek.controls.search.tooltip.advanced.example.suffix")));
    }

    @Override
    public boolean charTyped(char var1, int var2) {
        return search.charTyped(var1, var2);
    }

    @Override
    public void tick() {
        this.search.tick();
        if (!this.searchQuery.equals(search.getValue())) {
            this.searchQuery = search.getValue();
            ((JEKControlList) ((IJEKControlScreenExtensions) this).jek$getControlList()).search(this.searchQuery.toLowerCase(Locale.ROOT));
        }
    }
}

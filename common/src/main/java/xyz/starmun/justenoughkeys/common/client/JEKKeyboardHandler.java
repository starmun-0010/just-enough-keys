package xyz.starmun.justenoughkeys.common.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMapping;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;

public class JEKKeyboardHandler extends KeyboardHandler {
    private Minecraft minecraft;

    public JEKKeyboardHandler(Minecraft minecraft) {
        super(minecraft);
        this.minecraft = minecraft;
    }

    @Override
    public void keyPress(long l, int i, int j, int k, int m) {
        super.keyPress(l, i, j, k, m);
        if (l == this.minecraft.getWindow().getWindow()
                && (this.minecraft.screen == null
                || this.minecraft.screen.passEvents)) {

            InputConstants.Key key = InputConstants.getKey(i, j);
            IJEKKeyMapping.CURRENT_PRESSED_MODIFIERS.set(ModifierKey.modifierKeyFromValue(key.getValue()), k != 0);
            IJEKKeyMapping.set(key, k != 0);
            if (k != 0) {
                IJEKKeyMapping.click(key);
            }
        }
    }
}
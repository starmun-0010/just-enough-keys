package xyz.starmun.justenoughkeys.common.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;

public class JEKKeyboardHandler  {
//    private Minecraft minecraft;
//
//    public JEKKeyboardHandler(Minecraft minecraft) {
//        super(minecraft);
//        this.minecraft = minecraft;
//    }
//
//    @Override
//    public void keyPress(long windowHandle, int i, int j, int state, int m) {
//        if (windowHandle == this.minecraft.getWindow().getWindow()) {
//
//            ContainerEventHandler containerEventHandler = this.minecraft.screen;
//            if(containerEventHandler != null) {
//                if (state != 1 && state == 0 && containerEventHandler.keyReleased(i, j, m)) {
//                    return;
//                } else if (containerEventHandler.keyPressed(i, j, m)) {
//                    return;
//                }
//            }
//            super.keyPress(windowHandle, i, j, state, m);
//
//            if (this.minecraft.screen == null || this.minecraft.screen.passEvents) {
//                InputConstants.Key key = InputConstants.getKey(i, j);
//                IJEKKeyMappingExtensions.CURRENT_PRESSED_MODIFIERS.set(ModifierKey.modifierKeyFromValue(key.getValue()), state != 0);
//                IJEKKeyMappingExtensions.set(key, state != 0);
//                if (state != 0) {
//                    IJEKKeyMappingExtensions.click(key);
//                }
//            }
//        }
//    }
}
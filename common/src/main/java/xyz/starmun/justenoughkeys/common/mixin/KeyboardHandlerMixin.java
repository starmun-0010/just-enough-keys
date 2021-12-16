package xyz.starmun.justenoughkeys.common.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(method = "keyPress", at=@At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getWindow()J", shift = At.Shift.AFTER, ordinal = 0))
    public void keyPress(long l, int i, int j, int k, int m, CallbackInfo ci){
        InputConstants.Key key = InputConstants.getKey(i, j);
        if (ModifierKey.isModifierKey(key)) {
            IJEKKeyMappingExtensions.CURRENT_PRESSED_MODIFIERS.set(key,  k==1);
        }
    }

}

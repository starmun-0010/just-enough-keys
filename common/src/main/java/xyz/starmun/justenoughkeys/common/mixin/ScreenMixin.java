package xyz.starmun.justenoughkeys.common.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.starmun.justenoughkeys.common.data.ModifierKey;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "keyPressed", at = @At("TAIL"), cancellable = true)
    public void keyPressed(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        if (ModifierKey.isModifierKey(InputConstants.getKey(i, j))) {
            cir.setReturnValue(false);
        }
    }
}

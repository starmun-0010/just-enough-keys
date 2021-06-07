package xyz.starmun.justenoughkeys.mixin.shared;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.controls.ControlList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.starmun.justenoughkeys.JustEnoughKeys;

@Mixin(ControlList.KeyEntry.class)
public class KeyEntryMixin {

    @Shadow
    @Final
    private KeyMapping key;
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "lambda$new$0", at = @At("RETURN"), remap = false)
    public void onEdit(KeyMapping keyBinding, Button buttonWidget, CallbackInfo callbackInfo) {

    }
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "lambda$new$1", at = @At("RETURN"), remap = false)
    public void onReset(KeyMapping keyBinding, Button buttonWidget, CallbackInfo callbackInfo) {

    }
}

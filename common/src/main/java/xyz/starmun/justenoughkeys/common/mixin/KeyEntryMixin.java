package xyz.starmun.justenoughkeys.common.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.controls.ControlList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

@Pseudo
@Mixin(ControlList.KeyEntry.class)
@Implements(@Interface(iface = GuiEventListener.class, prefix = "jek$"))
public class KeyEntryMixin{

    @Shadow
    @Final
    private KeyMapping key;
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "lambda$new$0", at = @At("RETURN"), remap = false)
    public void onEdit(KeyMapping keyMapping, Button buttonWidget, CallbackInfo callbackInfo) {
        ((IJEKKeyMappingExtensions)keyMapping).jek$getModifierKeyMap().clear();
        keyMapping.setKey(InputConstants.UNKNOWN);
    }
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "lambda$new$1", at = @At("RETURN"), remap = false)
    public void onReset(KeyMapping keyMapping, Button buttonWidget, CallbackInfo callbackInfo) {
        ((IJEKKeyMappingExtensions)keyMapping).jek$getModifierKeyMap().clear();

    }

}

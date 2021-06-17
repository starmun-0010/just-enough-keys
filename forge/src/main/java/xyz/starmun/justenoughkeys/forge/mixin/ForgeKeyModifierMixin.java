package xyz.starmun.justenoughkeys.forge.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;

@Mixin(targets ={ "net/minecraftforge/client/settings/KeyModifier$1",
        "net/minecraftforge/client/settings/KeyModifier$2",
        "net/minecraftforge/client/settings/KeyModifier$3",
        "net/minecraftforge/client/settings/KeyModifier$4"} )
public class ForgeKeyModifierMixin {

    @Inject(method = "isActive", at=@At("HEAD"),cancellable = true, remap = false)
    public void isActive(@Nullable IKeyConflictContext conflictContext, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }

}

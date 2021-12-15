package xyz.starmun.justenoughkeys.forge.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    //Disable forge drop hovered item behaviour, in inventory
    @Redirect( method = "keyPressed", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isActiveAndMatches(Lcom/mojang/blaze3d/platform/InputConstants$Key;)Z", ordinal = 2))
    public boolean matchesDrop(KeyMapping instance, InputConstants.Key key){
        return false;
    }
 }

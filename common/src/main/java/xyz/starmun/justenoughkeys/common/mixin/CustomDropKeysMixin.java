package xyz.starmun.justenoughkeys.common.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.starmun.justenoughkeys.common.client.JEKControls;

@Mixin(Minecraft.class)
public class CustomDropKeysMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow @Final
    public Options options;

    //Disable vanilla drop held item behaviour
    @Redirect(method = "handleKeybinds", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z", ordinal = 7))
    public boolean consumeDropClick(KeyMapping instance){
        return false;
    }
    @Inject(method = "handleKeybinds", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z", ordinal = 7))
    public void handleDropKeys(CallbackInfo ci){
        while(this.options.keyDrop.consumeClick()) {// 1675
            assert this.player != null;
            if (!this.player.isSpectator() && this.player.drop(false)) {// 1676 1677
                this.player.swing(InteractionHand.MAIN_HAND);// 1678
            }
        }
        while (JEKControls.dropStack.consumeClick()){
            assert this.player != null;
            if (!this.player.isSpectator() && this.player.drop(true)) {// 1676 1677
                this.player.swing(InteractionHand.MAIN_HAND);// 1678
            }
        }
    }
}

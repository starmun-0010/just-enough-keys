package xyz.starmun.justenoughkeys.fabric.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.starmun.justenoughkeys.common.JEKControlScreen;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin {

    //Replace default control screen with our custom control screen on Controls button clicked event
    //lambda$init$6 is the original event handler
//    @SuppressWarnings("UnresolvedMixinReference")
//    @Redirect(method = "lambda$init$6", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"), remap = false)
//    public void setScreen(Minecraft minecraft, Screen screen){
//        try {
//            if(screen instanceof ControlsScreen && !(screen instanceof JEKControlScreen)) {
//                minecraft.setScreen(new JEKControlScreen(minecraft.screen, minecraft.options));
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
}

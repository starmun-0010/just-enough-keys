package xyz.starmun.justenoughkeys.common.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.starmun.justenoughkeys.common.client.JEKControls;
import xyz.starmun.justenoughkeys.common.gui.JEKControlScreen;

@Mixin(value = Minecraft.class)
public class MinecraftMixin {

    @ModifyVariable(method = "setScreen", at=@At("HEAD"), argsOnly = true)
    public Screen setScreen(Screen screen){
        try {
            if(screen instanceof ControlsScreen && !(screen instanceof JEKControlScreen)) {
                return new JEKControlScreen(Minecraft.getInstance().screen, Minecraft.getInstance().options);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return screen;
    }
}

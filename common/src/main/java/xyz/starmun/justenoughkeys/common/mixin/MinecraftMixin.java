package xyz.starmun.justenoughkeys.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.starmun.justenoughkeys.common.gui.JEKControlScreen;

@Mixin(value = Minecraft.class)
public class MinecraftMixin {
    @Shadow @Nullable public Screen screen;

    @ModifyVariable(method = "setScreen", at=@At("HEAD"))
    public Screen setScreen(Screen screen){
        try {
            if(screen instanceof KeyBindsScreen && !(screen instanceof JEKControlScreen)) {
                JEKControlScreen controlsScreen = new JEKControlScreen(Minecraft.getInstance().screen, Minecraft.getInstance().options);
                return controlsScreen;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return screen;
    }
}

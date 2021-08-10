package xyz.starmun.justenoughkeys.common.mixin;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.starmun.justenoughkeys.common.contracts.IJEKScreenExtensions;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin implements IJEKScreenExtensions {
    @Shadow
    @Final
    private List<Widget> renderables;
    @Override
    public List<Widget> jek$getRenderables(){
        return renderables;
    }
}
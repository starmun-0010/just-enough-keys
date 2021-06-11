package xyz.starmun.justenoughkeys.common.mixin;

import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import xyz.starmun.justenoughkeys.common.contracts.IJEKOptionsSubScreenExtensions;

@Mixin(OptionsSubScreen.class)
public class OptionsSubScreenMixin extends Screen implements IJEKOptionsSubScreenExtensions {

    @Shadow
    @Final
    protected Screen lastScreen;

    protected OptionsSubScreenMixin(Component component) {
        super(component);
    }

    @Unique
    public Screen jek$getLastScreen() {
        return lastScreen;
    }

    @Override
    public void removed(){
        if(this.minecraft!=null && this.minecraft.options != null)this.minecraft.options.save();
    }

}

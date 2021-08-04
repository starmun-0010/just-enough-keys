package xyz.starmun.justenoughkeys.mixin;

import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OptionsSubScreen.class)
public class OptionsSubScreenMixin extends Screen   {

    protected OptionsSubScreenMixin(Component component) {
        super(component);
    }

    @Override
    public void removed(){
        if(this.minecraft!=null && this.minecraft.options != null)this.minecraft.options.save();
    }

}

package xyz.starmun.justenoughkeys.mixin;

import net.minecraft.client.gui.screens.controls.ControlList;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.starmun.justenoughkeys.contracts.IJEKControlScreenExtensions;

@Mixin(ControlsScreen.class)
public class ControlsScreenMixin implements IJEKControlScreenExtensions {

    @Shadow
    private ControlList controlList;

    @Override
    public ControlList jek$getControlList() {
       return controlList;
    }

    @Override
    public void jek$setControlList(ControlList controlsList) {
        this.controlList = controlsList;
    }
}

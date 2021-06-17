package xyz.starmun.justenoughkeys.common.mixin;

import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlList;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import xyz.starmun.justenoughkeys.common.contracts.IJEKControlScreenExtensions;
import xyz.starmun.justenoughkeys.common.contracts.IJEKOptionsSubScreenExtensions;

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

package xyz.starmun.justenoughkeys.common.mixin;

import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.starmun.justenoughkeys.common.contracts.IJEKControlScreenExtensions;

@Mixin(KeyBindsScreen.class)
public class ControlsScreenMixin implements IJEKControlScreenExtensions {

    @Shadow
    private KeyBindsList keyBindsList;

    @Override
    public KeyBindsList jek$getControlList() {
       return keyBindsList;
    }

    @Override
    public void jek$setControlList(KeyBindsList controlsList) {
        this.keyBindsList = controlsList;
    }
}

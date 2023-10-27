package xyz.starmun.justenoughkeys.common.contracts;

import net.minecraft.client.gui.screens.controls.KeyBindsList;

public interface IJEKControlScreenExtensions {
    KeyBindsList jek$getControlList();

    void jek$setControlList(KeyBindsList controlsList);
}

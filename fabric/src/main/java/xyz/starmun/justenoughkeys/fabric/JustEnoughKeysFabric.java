package xyz.starmun.justenoughkeys.fabric;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import net.fabricmc.api.ModInitializer;

public class JustEnoughKeysFabric implements ModInitializer {
    @Override
    public void onInitialize() {

        JustEnoughKeys.init();
        KeyBindingHelper.registerKeyBinding(JustEnoughKeys.dropStack);
    }
}

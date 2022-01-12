package xyz.starmun.justenoughkeys.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import xyz.starmun.justenoughkeys.common.client.Config;
import xyz.starmun.justenoughkeys.common.client.JEKControls;

public class JustEnoughKeysClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if(Config.isCustomDropKeyFeatureEnabled()){
            KeyBindingHelper.registerKeyBinding(JEKControls.dropStack);
        }
    }
}

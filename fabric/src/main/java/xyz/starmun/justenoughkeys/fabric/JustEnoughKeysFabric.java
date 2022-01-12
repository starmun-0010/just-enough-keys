package xyz.starmun.justenoughkeys.fabric;

import net.fabricmc.api.ModInitializer;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;

public class JustEnoughKeysFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        JustEnoughKeys.init();
    }
}

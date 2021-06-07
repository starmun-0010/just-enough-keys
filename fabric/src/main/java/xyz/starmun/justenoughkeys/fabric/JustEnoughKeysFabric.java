package xyz.starmun.justenoughkeys.fabric;

import xyz.starmun.justenoughkeys.JustEnoughKeys;
import net.fabricmc.api.ModInitializer;

public class JustEnoughKeysFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        JustEnoughKeys.init();
    }
}

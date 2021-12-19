package xyz.starmun.justenoughkeys.platform.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ModsImpl {
    public static boolean isLoaded(String modId){
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}

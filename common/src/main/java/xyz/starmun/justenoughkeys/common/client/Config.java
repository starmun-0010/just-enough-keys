package xyz.starmun.justenoughkeys.common.client;

import xyz.starmun.justenoughkeys.platform.Mods;

public class Config {
    public static boolean isCustomDropKeyFeatureEnabled() {
        return !(  Mods.isLoaded("dontdropit")
                || Mods.isLoaded("interactic")
        );
    }
}

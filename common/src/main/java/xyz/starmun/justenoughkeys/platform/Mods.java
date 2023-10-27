package xyz.starmun.justenoughkeys.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class Mods {
    @ExpectPlatform()
    public static boolean isLoaded(String modId) {
        return false;
    }
}

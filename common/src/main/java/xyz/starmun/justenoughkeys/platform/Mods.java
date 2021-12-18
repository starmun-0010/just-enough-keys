package xyz.starmun.justenoughkeys.platform;

import me.shedaniel.architectury.annotations.ExpectPlatform;

public class Mods {
    @ExpectPlatform()
    public static boolean isLoaded(String modId){
        return false;
    }
}

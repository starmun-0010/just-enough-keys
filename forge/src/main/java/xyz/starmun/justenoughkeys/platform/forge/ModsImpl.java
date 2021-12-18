package xyz.starmun.justenoughkeys.platform.forge;

import net.minecraftforge.fml.ModList;

public class ModsImpl {
    public static boolean isLoaded(String modID){
        return ModList.get().isLoaded(modID);
    }
}

package xyz.starmun.justenoughkeys.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import xyz.starmun.justenoughkeys.JustEnoughKeys;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(JustEnoughKeys.MOD_ID)
public class JustEnoughKeysForge {
    public JustEnoughKeysForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(JustEnoughKeys.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        JustEnoughKeys.init();
    }
}

package xyz.starmun.justenoughkeys.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.MinecraftForge;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.starmun.justenoughkeys.forge.events.handlers.ClientEventHandler;

@Mod(JustEnoughKeys.MOD_ID)
public class JustEnoughKeysForge {

    public JustEnoughKeysForge() {
        // Submit our event bus to let architectury register our content on the right time
       // MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

        EventBuses.registerModEventBus(JustEnoughKeys.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        JustEnoughKeys.init();
    }
}

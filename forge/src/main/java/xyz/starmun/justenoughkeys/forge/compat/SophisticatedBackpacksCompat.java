package xyz.starmun.justenoughkeys.forge.compat;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.fml.ModList;
import net.p3pp3rf1y.sophisticatedbackpacks.client.KeybindHandler;
import xyz.starmun.justenoughkeys.common.JustEnoughKeys;
import xyz.starmun.justenoughkeys.common.contracts.IJEKKeyMappingExtensions;

import java.util.Arrays;

public class SophisticatedBackpacksCompat {
    static {
        try{
            if (ModList.get().isLoaded("sophisticatedbackpacks")){
                Arrays.stream(KeybindHandler.class.getDeclaredFields()).forEach(field -> {
                    if(field.getType() == KeyMapping.class){
                        try {
                            KeyMapping keyMapping = (KeyMapping) field.get(KeybindHandler.class);
                            IJEKKeyMappingExtensions.ALL.put(keyMapping.getName(), keyMapping);
                            IJEKKeyMappingExtensions.initMAP(keyMapping);
                        }
                        catch (Exception ex){
                            JustEnoughKeys.LOGGER.error("Failed to setup Sophisticated Backpacks compatibility.");
                        }
                    }
                });
            }
        }
        catch (Exception ex){
            JustEnoughKeys.LOGGER.error("Failed to setup Sophisticated Backpacks compatibility.");
        }
    }
    public static void init(){

    }
}
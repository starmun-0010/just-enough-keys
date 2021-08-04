package xyz.starmun.justenoughkeys;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ModInitializer;

public class JustEnoughKeys implements ModInitializer {
    public static final String MOD_ID = "justenoughkeys";
    public static final String MOD_NAME = "Just Enough Keys";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static void init() {
        LOGGER.info("Loading Key Bindings");
    }
    @Override
    public void onInitialize() {

        this.init();
    }
}

package xyz.starmun.justenoughkeys;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JustEnoughKeys {
    public static final String MOD_ID = "justenoughkeys";
    public static final String MOD_NAME = "Just Enough Keys";
    // We can use this if we don't want to use DeferredRegister
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static void init() {
        LOGGER.debug("Loading Key Bindings");
        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}

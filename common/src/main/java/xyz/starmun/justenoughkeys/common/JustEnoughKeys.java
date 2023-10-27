package xyz.starmun.justenoughkeys.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JustEnoughKeys {
    public static final String MOD_ID = "justenoughkeys";
    public static final String MOD_NAME = "Just Enough Keys";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static void init() {
        LOGGER.info("Loading Key Bindings");
    }
}

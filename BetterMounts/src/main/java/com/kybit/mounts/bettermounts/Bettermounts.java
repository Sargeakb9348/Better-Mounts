package com.kybit.mounts.bettermounts;

import com.kybit.mounts.bettermounts.item.ModItems;
import com.kybit.mounts.bettermounts.util.ModRegistries;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

public class Bettermounts implements ModInitializer {

    public static String MOD_ID = "bettermounts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModRegistries.registerModStuff();
        GeckoLib.initialize();
    }
}

package com.kybit.mounts.bettermounts.util;

import com.kybit.mounts.bettermounts.entity.ModEntities;
import com.kybit.mounts.bettermounts.entity.custom.RaccoonEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModRegistries {
    public static void registerModStuff(){
        registerAttributes();
    }

    public static void registerAttributes(){
        FabricDefaultAttributeRegistry.register(ModEntities.RACCOON, RaccoonEntity.setAttributes());
    }

}

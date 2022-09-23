package com.kybit.mounts.bettermounts.entity;

import com.kybit.mounts.bettermounts.Bettermounts;
import com.kybit.mounts.bettermounts.entity.custom.RaccoonEntity;
import com.kybit.mounts.bettermounts.entity.custom.WolfEntity;
import net.minecraft.entity.EntityType;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModEntities {

    //Raccoon Entity
    public static final EntityType<RaccoonEntity> RACCOON = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Bettermounts.MOD_ID, "raccoon"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RaccoonEntity::new)
                    .dimensions(EntityDimensions.fixed(2.0f, 1.5f)).build());

    //Wolf Entity
    public static final EntityType<WolfEntity> WOLF = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Bettermounts.MOD_ID, "wolf"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, WolfEntity::new)
                    .dimensions(EntityDimensions.fixed(2.0f, 1.5f)).build());
}

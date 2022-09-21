package com.kybit.mounts.bettermounts.item;

import com.kybit.mounts.bettermounts.entity.ModEntities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

import static software.bernie.example.registry.RegistryUtils.registerItem;

public class ModItems {




    public static final Item RACCOON_SPAWN_EGG = registerItem("raccoon_spawn_egg", new SpawnEggItem(ModEntities.RACCOON,0x948e8d, 0x3b3635));
}

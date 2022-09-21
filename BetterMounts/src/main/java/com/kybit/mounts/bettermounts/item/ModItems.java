package com.kybit.mounts.bettermounts.item;

import com.kybit.mounts.bettermounts.Bettermounts;
import com.kybit.mounts.bettermounts.entity.ModEntities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static software.bernie.example.registry.RegistryUtils.registerItem;

public class ModItems {

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Bettermounts.MOD_ID, name), item);
    }
    public static void registerModItems() {
        Bettermounts.LOGGER.info("Registering Mod Items for " + Bettermounts.MOD_ID);
    }
    public static final Item RACCOON_SPAWN_EGG = registerItem("raccoon_spawn_egg", new SpawnEggItem(ModEntities.RACCOON,0x948e8d, 0x3b3635, new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));


}

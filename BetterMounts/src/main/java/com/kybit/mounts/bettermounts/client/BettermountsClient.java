package com.kybit.mounts.bettermounts.client;

import com.kybit.mounts.bettermounts.entity.ModEntities;
import com.kybit.mounts.bettermounts.entity.client.RaccoonRenderer;
import com.kybit.mounts.bettermounts.entity.client.WolfRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class BettermountsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(ModEntities.RACCOON, RaccoonRenderer::new); //render call for raccoon
        EntityRendererRegistry.register(ModEntities.WOLF, WolfRenderer::new);//render call for wolf
    }
}

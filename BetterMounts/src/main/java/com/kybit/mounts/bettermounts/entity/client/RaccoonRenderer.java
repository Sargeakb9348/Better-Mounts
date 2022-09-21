package com.kybit.mounts.bettermounts.entity.client;

import com.kybit.mounts.bettermounts.Bettermounts;
import com.kybit.mounts.bettermounts.entity.custom.RaccoonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RaccoonRenderer extends GeoEntityRenderer <RaccoonEntity> {
    public RaccoonRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RaccoonModel());
    }

    @Override
    public Identifier getTextureResource(RaccoonEntity entity) {
        return new Identifier(Bettermounts.MOD_ID, "textures/entity/raccoon/raccoon.png");
    }
}

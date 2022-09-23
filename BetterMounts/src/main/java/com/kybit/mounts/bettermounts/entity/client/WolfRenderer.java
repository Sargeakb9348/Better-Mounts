package com.kybit.mounts.bettermounts.entity.client;

import com.kybit.mounts.bettermounts.Bettermounts;
import com.kybit.mounts.bettermounts.entity.custom.RaccoonEntity;
import com.kybit.mounts.bettermounts.entity.custom.WolfEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WolfRenderer  extends GeoEntityRenderer<WolfEntity> {
    public WolfRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WolfModel());
    }

    @Override
    public Identifier getTextureResource(WolfEntity entity) {
        return new Identifier(Bettermounts.MOD_ID, "textures/entity/wolf/wolf.png");
    }

}


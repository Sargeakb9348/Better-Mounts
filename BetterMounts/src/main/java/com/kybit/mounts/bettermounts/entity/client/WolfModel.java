package com.kybit.mounts.bettermounts.entity.client;

import com.kybit.mounts.bettermounts.Bettermounts;
import com.kybit.mounts.bettermounts.entity.custom.RaccoonEntity;
import com.kybit.mounts.bettermounts.entity.custom.WolfEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class WolfModel extends AnimatedGeoModel <WolfEntity> {
    @Override
    public Identifier getModelResource(WolfEntity object) { //geoJSON file
        return new Identifier(Bettermounts.MOD_ID, "geo/wolf.geo.json");
    }

    @Override
    public Identifier getTextureResource(WolfEntity object) { //Texture file
        return new Identifier(Bettermounts.MOD_ID, "textures/entity/wolf/wolf.png");
    }

    @Override
    public Identifier getAnimationResource(WolfEntity animatable) {//animation file
        return new Identifier(Bettermounts.MOD_ID, "animations/raccoon.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(WolfEntity entity, Integer uniqueID, AnimationEvent customPredicate) { //look at player animation
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
        }
    }
}

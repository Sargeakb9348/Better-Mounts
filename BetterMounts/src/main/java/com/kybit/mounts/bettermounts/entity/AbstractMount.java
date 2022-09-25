package com.kybit.mounts.bettermounts.entity;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.RideableInventory;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.HorseBondWithPlayerGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMount extends AnimalEntity implements JumpingMount, Saddleable {


    private static final TrackedData<Byte> MOUNT_FLAGS;
    private static final int SADDLED_FLAG = 4;
    protected boolean inAir;
    protected SimpleInventory items;
    protected float MOUNT_JUMP_STRENGTH;
    private boolean jumping;
    private float jumpStrength;

    protected AbstractMount(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MOUNT_FLAGS, (byte)0);
        //this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
    }

    protected boolean getMountFlag(int bitmask) {
        return ((Byte)this.dataTracker.get(MOUNT_FLAGS) & bitmask) != 0;
    }

    protected void setMountFlag(int bitmask, boolean flag) {
        byte b = (Byte)this.dataTracker.get(MOUNT_FLAGS);
        if (flag) {
            this.dataTracker.set(MOUNT_FLAGS, (byte)(b | bitmask));
        } else {
            this.dataTracker.set(MOUNT_FLAGS, (byte)(b & ~bitmask));
        }

    }

    @Override
    public boolean canBeSaddled() {
        return this.isAlive() && !this.isBaby(); //&& this.isTame();
    }

    @Override
    public void saddle(@Nullable SoundCategory sound) {
        this.items.setStack(0, new ItemStack(Items.SADDLE));
        if (sound != null) {
            this.world.playSoundFromEntity((PlayerEntity)null, this, SoundEvents.ENTITY_HORSE_SADDLE, sound, 0.5F, 1.0F);
        }
    }

    @Override
    public boolean isSaddled() {
        return this.getMountFlag(4);
    }


    protected void updateSaddle() {
        if (!this.world.isClient) {
            this.setMountFlag(4, !this.items.getStack(0).isEmpty());
        }
    }

    public void onInventoryChanged(Inventory sender) {
        boolean bl = this.isSaddled();
        this.updateSaddle();
        if (this.age > 20 && !bl && this.isSaddled()) {
            this.playSound(SoundEvents.ENTITY_HORSE_SADDLE, 0.5F, 1.0F);
        }

    }

    public ActionResult interactMount(PlayerEntity player, ItemStack stack) {
        boolean bl = this.receiveFood(player, stack);
        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        if (this.world.isClient) {
            return ActionResult.CONSUME;
        } else {
            return bl ? ActionResult.SUCCESS : ActionResult.PASS;
        }
    }

    protected abstract boolean receiveFood(PlayerEntity player, ItemStack item);// What this abstract method looks like for horses v

//    boolean bl = false;
//    float f = 0.0F;
//    int i = 0;
//    int j = 0;
//        if (item.isOf(Items.WHEAT)) {
//        f = 2.0F;
//        i = 20;
//        j = 3;
//    } else if (item.isOf(Items.SUGAR)) {
//        f = 1.0F;
//        i = 30;
//        j = 3;
//    } else if (item.isOf(Blocks.HAY_BLOCK.asItem())) {
//        f = 20.0F;
//        i = 180;
//    } else if (item.isOf(Items.APPLE)) {
//        f = 3.0F;
//        i = 60;
//        j = 3;
//    } else if (item.isOf(Items.GOLDEN_CARROT)) {
//        f = 4.0F;
//        i = 60;
//        j = 5;
//        if (!this.world.isClient && this.isTame() && this.getBreedingAge() == 0 && !this.isInLove()) {
//            bl = true;
//            this.lovePlayer(player);
//        }
//    } else if (item.isOf(Items.GOLDEN_APPLE) || item.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
//        f = 10.0F;
//        i = 240;
//        j = 10;
//        if (!this.world.isClient && this.isTame() && this.getBreedingAge() == 0 && !this.isInLove()) {
//            bl = true;
//            this.lovePlayer(player);
//        }
//    }
//
//        if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
//        this.heal(f);
//        bl = true;
//    }
//
//        if (this.isBaby() && i > 0) {
//        this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), 0.0, 0.0, 0.0);
//        if (!this.world.isClient) {
//            this.growUp(i);
//        }
//
//        bl = true;
//    }
//
//        if (j > 0 && (bl || !this.isTame()) && this.getTemper() < this.getMaxTemper()) {
//        bl = true;
//        if (!this.world.isClient) {
//            this.addTemper(j);
//        }
//    }
//
//        if (bl) {
//        this.playEatingAnimation();
//        this.emitGameEvent(GameEvent.EAT);
//    }
//
//        return bl;

    protected void putPlayerOnBack(PlayerEntity player) {
//        this.setEatingGrass(false);
//        this.setAngry(false);
        //Bettermounts.logger.info("test1");
        if (!this.world.isClient) {
            player.setYaw(this.getYaw());
            player.setPitch(this.getPitch());
            player.startRiding(this);
        }

    }

    public void travel(Vec3d movementInput) {
        if (this.isAlive()) {
            LivingEntity livingEntity = this.getPrimaryPassenger();
            if (this.hasPassengers() && livingEntity != null) {
                this.setYaw(livingEntity.getYaw());
                this.prevYaw = this.getYaw();
                this.setPitch(livingEntity.getPitch() * 0.5F);
                this.setRotation(this.getYaw(), this.getPitch());
                this.bodyYaw = this.getYaw();
                this.headYaw = this.bodyYaw;
                float f = livingEntity.sidewaysSpeed * 0.5F;
                float g = livingEntity.forwardSpeed;
                if (g <= 0.0F) {
                    g *= 0.25F;
                    //this.soundTicks = 0;
                }

                /*if (this.onGround && this.MOUNT_JUMP_STRENGTH == 0.0F && !this.jumping) {
                    f = 0.0F;
                    g = 0.0F;
                }*/

                if (this.MOUNT_JUMP_STRENGTH > 0.0F && !this.isInAir() && this.onGround) {
                    double d = this.getJumpStrength() * (double)this.MOUNT_JUMP_STRENGTH * (double)this.getJumpVelocityMultiplier();
                    double e = d + this.getJumpBoostVelocityModifier();
                    Vec3d vec3d = this.getVelocity();
                    this.setVelocity(vec3d.x, e, vec3d.z);
                    this.setInAir(true);
                    this.velocityDirty = true;
                    if (g > 0.0F) {
                        float h = MathHelper.sin(this.getYaw() * 0.017453292F);
                        float i = MathHelper.cos(this.getYaw() * 0.017453292F);
                        this.setVelocity(this.getVelocity().add((double)(-0.4F * h * this.MOUNT_JUMP_STRENGTH), 0.0, (double)(0.4F * i * this.MOUNT_JUMP_STRENGTH)));
                    }

                    this.MOUNT_JUMP_STRENGTH = 0.0F;
                }

                this.airStrafingSpeed = this.getMovementSpeed() * 0.1F;
                if (this.isLogicalSideForUpdatingMovement()) {
                    this.setMovementSpeed((float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                    super.travel(new Vec3d((double)f, movementInput.y, (double)g));
                } else if (livingEntity instanceof PlayerEntity) {
                    this.setVelocity(Vec3d.ZERO);
                }

                if (this.onGround) {
                    this.MOUNT_JUMP_STRENGTH = 0.0F;
                    this.setInAir(false);
                }

                this.updateLimbs(this, false);
                this.tryCheckBlockCollision();
            } else {
                this.airStrafingSpeed = 0.02F;
                super.travel(movementInput);
            }
        }
    }

    public float getJumpStrength() {
        return MOUNT_JUMP_STRENGTH;
    }

    @Override
    public void setJumpStrength(int strength) {
        if (this.isSaddled()) {
            if (strength < 0) {
                strength = 0;
            } else {
                this.jumping = true;

            }

            if (strength >= 90) {
                this.MOUNT_JUMP_STRENGTH = 1.0F;
            } else {
                this.MOUNT_JUMP_STRENGTH = 0.4F + 0.4F * (float)strength / 90.0F;
            }

        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void startJumping(int height) {
        this.jumping = true;
    }

    @Override
    public void stopJumping() {
    }

    public boolean isInAir() {
        return this.inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public void updatePassengerPosition(Entity passenger) {
        super.updatePassengerPosition(passenger);
        if (passenger instanceof MobEntity mobEntity) {
            this.bodyYaw = mobEntity.bodyYaw;
        }

//        if (this.lastAngryAnimationProgress > 0.0F) {
//            float f = MathHelper.sin(this.bodyYaw * 0.017453292F);
//            float g = MathHelper.cos(this.bodyYaw * 0.017453292F);
//            float h = 0.7F * this.lastAngryAnimationProgress;
//            float i = 0.15F * this.lastAngryAnimationProgress;
//            passenger.setPosition(this.getX() + (double)(h * f), this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset() + (double)i, this.getZ() - (double)(h * g));
//            if (passenger instanceof LivingEntity) {
//                ((LivingEntity)passenger).bodyYaw = this.bodyYaw;
//            }
//        }

    }

    @Nullable
    public LivingEntity getPrimaryPassenger() {
        //if (this.isSaddled()) {
            Entity var2 = this.getFirstPassenger();
            if (var2 instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)var2;
                return livingEntity;
            }
        //}

        return null;
    }

    @Nullable
    private Vec3d locateSafeDismountingPos(Vec3d offset, LivingEntity passenger) {
        double d = this.getX() + offset.x;
        double e = this.getBoundingBox().minY;
        double f = this.getZ() + offset.z;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        UnmodifiableIterator var10 = passenger.getPoses().iterator();

        while(var10.hasNext()) {
            EntityPose entityPose = (EntityPose)var10.next();
            mutable.set(d, e, f);
            double g = this.getBoundingBox().maxY + 0.75;

            while(true) {
                double h = this.world.getDismountHeight(mutable);
                if ((double)mutable.getY() + h > g) {
                    break;
                }

                if (Dismounting.canDismountInBlock(h)) {
                    Box box = passenger.getBoundingBox(entityPose);
                    Vec3d vec3d = new Vec3d(d, (double)mutable.getY() + h, f);
                    if (Dismounting.canPlaceEntityAt(this.world, passenger, box.offset(vec3d))) {
                        passenger.setPose(entityPose);
                        return vec3d;
                    }
                }

                mutable.move(Direction.UP);
                if (!((double)mutable.getY() < g)) {
                    break;
                }
            }
        }

        return null;
    }

    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d vec3d = getPassengerDismountOffset((double)this.getWidth(), (double)passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.RIGHT ? 90.0F : -90.0F));
        Vec3d vec3d2 = this.locateSafeDismountingPos(vec3d, passenger);
        if (vec3d2 != null) {
            return vec3d2;
        } else {
            Vec3d vec3d3 = getPassengerDismountOffset((double)this.getWidth(), (double)passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.LEFT ? 90.0F : -90.0F));
            Vec3d vec3d4 = this.locateSafeDismountingPos(vec3d3, passenger);
            return vec3d4 != null ? vec3d4 : this.getPos();
        }
    }

    protected void initAttributes(Random random) {
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (entityData == null) {
            entityData = new PassiveEntity.PassiveData(0.2F);
        }

        this.initAttributes(world.getRandom());
        return super.initialize(world, difficulty, spawnReason, (EntityData)entityData, entityNbt);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    static {
        MOUNT_FLAGS = DataTracker.registerData(AbstractMount.class, TrackedDataHandlerRegistry.BYTE);
        //OWNER_UUID = DataTracker.registerData(AbstractHorseEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    }
}

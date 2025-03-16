package net.minecraft.client.renderer;

import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockReader;
import net.optifine.reflect.Reflector;
import org.excellent.client.managers.events.other.CameraClipEvent;
import org.excellent.client.managers.events.player.RotationEvent;
import org.excellent.client.managers.module.impl.misc.CameraClip;
import org.excellent.client.utils.animation.Animation;
import org.excellent.client.utils.animation.util.Easings;
import org.excellent.client.utils.math.Mathf;

public class ActiveRenderInfo {
    private boolean valid;
    private IBlockReader world;
    private Entity renderViewEntity;
    private Vector3d pos = Vector3d.ZERO;
    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private final Vector3f look = new Vector3f(0.0F, 0.0F, 1.0F);
    private final Vector3f up = new Vector3f(0.0F, 1.0F, 0.0F);
    private final Vector3f left = new Vector3f(1.0F, 0.0F, 0.0F);
    private float pitch;
    private float yaw;
    private final Quaternion rotation = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
    private boolean thirdPerson;
    @Getter
    private boolean thirdPersonReverse;
    private float height;
    private float previousHeight;
    private final Animation zoomAnimation = new Animation();
    public double zoomWheel = 0.0F;

    private double getUpdatedSmoothZooming(boolean zoomActive) {
        zoomWheel = Mathf.clamp(0.0F, 50.0F, zoomWheel);
        if (!zoomActive) zoomWheel = 0.0F;

        float zoomValue = (zoomActive ? 4.0F : 1.0F);

        float to = (float) (zoomValue + zoomWheel);
        zoomAnimation.update();
        zoomAnimation.run(to, zoomActive ? 0.5F : 0.0F, Easings.SINE_OUT, true);

        return zoomAnimation.getValue();
    }

    public void update(IBlockReader worldIn, Entity renderViewEntity, boolean thirdPersonIn, boolean thirdPersonReverseIn, float partialTicks) {
        this.valid = true;
        this.world = worldIn;
        this.renderViewEntity = renderViewEntity;
        this.thirdPerson = thirdPersonIn;
        this.thirdPersonReverse = thirdPersonReverseIn;
        RotationEvent event = new RotationEvent(renderViewEntity.getYaw(partialTicks), renderViewEntity.getPitch(partialTicks), partialTicks);
        event.hook();

        this.setDirection(event.getYaw(), event.getPitch());
        this.setPosition(MathHelper.lerp(partialTicks, renderViewEntity.prevPosX, renderViewEntity.getPosX()), MathHelper.lerp(partialTicks, renderViewEntity.prevPosY, renderViewEntity.getPosY()) + (double) MathHelper.lerp(partialTicks, this.previousHeight, this.height), MathHelper.lerp(partialTicks, renderViewEntity.prevPosZ, renderViewEntity.getPosZ()));
        double zoomDistance = CameraClip.getInstance().isEnabled() ? getUpdatedSmoothZooming(thirdPersonIn) : 4F;
        if (thirdPersonIn) {
            if (thirdPersonReverseIn) {
                this.setDirection(event.getYaw() + 180.0F, -event.getPitch());
            }

            this.movePosition(-this.calcCameraDistance(zoomDistance), 0.0D, 0.0D);
        } else if (renderViewEntity instanceof LivingEntity && ((LivingEntity) renderViewEntity).isSleeping()) {
            Direction direction = ((LivingEntity) renderViewEntity).getBedDirection();
            this.setDirection(direction != null ? direction.getHorizontalAngle() - 180.0F : 0.0F, 0.0F);
            this.movePosition(0.0D, 0.3D, 0.0D);
        }
    }

    public void interpolateHeight() {
        if (this.renderViewEntity != null) {
            this.previousHeight = this.height;
            this.height += (this.renderViewEntity.getEyeHeight() - this.height) * 0.5F;
        }
    }

    /**
     * Checks for collision of the third person camera and returns the distance
     */
    private double calcCameraDistance(double startingDistance) {
        CameraClipEvent event = new CameraClipEvent();
        event.hook();
        for (int i = 0; i < 8; ++i) {
            float f = (float) ((i & 1) * 2 - 1);
            float f1 = (float) ((i >> 1 & 1) * 2 - 1);
            float f2 = (float) ((i >> 2 & 1) * 2 - 1);
            f = f * 0.1F;
            f1 = f1 * 0.1F;
            f2 = f2 * 0.1F;
            Vector3d vector3d = this.pos.add(f, f1, f2);
            Vector3d vector3d1 = new Vector3d(this.pos.x - (double) this.look.getX() * startingDistance + (double) f + (double) f2, this.pos.y - (double) this.look.getY() * startingDistance + (double) f1, this.pos.z - (double) this.look.getZ() * startingDistance + (double) f2);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.NONE, this.renderViewEntity));

            if (!event.isCancelled()) {
                if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
                    double d0 = raytraceresult.getHitVec().distanceTo(this.pos);

                    if (d0 < startingDistance) {
                        startingDistance = d0;
                    }
                }
            }
        }

        return startingDistance;
    }

    /**
     * Moves the render position relative to the view direction, for third person camera
     */
    protected void movePosition(double distanceOffset, double verticalOffset, double horizontalOffset) {
        double d0 = (double) this.look.getX() * distanceOffset + (double) this.up.getX() * verticalOffset + (double) this.left.getX() * horizontalOffset;
        double d1 = (double) this.look.getY() * distanceOffset + (double) this.up.getY() * verticalOffset + (double) this.left.getY() * horizontalOffset;
        double d2 = (double) this.look.getZ() * distanceOffset + (double) this.up.getZ() * verticalOffset + (double) this.left.getZ() * horizontalOffset;
        this.setPosition(new Vector3d(this.pos.x + d0, this.pos.y + d1, this.pos.z + d2));
    }

    protected void setDirection(float pitchIn, float yawIn) {
        this.pitch = yawIn;
        this.yaw = pitchIn;
        this.rotation.set(0.0F, 0.0F, 0.0F, 1.0F);
        this.rotation.multiply(Vector3f.YP.rotationDegrees(-pitchIn));
        this.rotation.multiply(Vector3f.XP.rotationDegrees(yawIn));
        this.look.set(0.0F, 0.0F, 1.0F);
        this.look.transform(this.rotation);
        this.up.set(0.0F, 1.0F, 0.0F);
        this.up.transform(this.rotation);
        this.left.set(1.0F, 0.0F, 0.0F);
        this.left.transform(this.rotation);
    }

    /**
     * Sets the position and blockpos of the active render
     */
    protected void setPosition(double x, double y, double z) {
        this.setPosition(new Vector3d(x, y, z));
    }

    protected void setPosition(Vector3d posIn) {
        this.pos = posIn;
        this.blockPos.setPos(posIn.x, posIn.y, posIn.z);
    }

    public Vector3d getProjectedView() {
        return this.pos;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public Quaternion getRotation() {
        return this.rotation;
    }

    public Entity getRenderViewEntity() {
        return this.renderViewEntity;
    }

    public boolean isValid() {
        return this.valid;
    }

    public boolean isThirdPerson() {
        return this.thirdPerson;
    }

    public FluidState getFluidState() {
        if (!this.valid) {
            return Fluids.EMPTY.getDefaultState();
        } else {
            FluidState fluidstate = this.world.getFluidState(this.blockPos);
            return !fluidstate.isEmpty() && this.pos.y >= (double) ((float) this.blockPos.getY() + fluidstate.getActualHeight(this.world, this.blockPos)) ? Fluids.EMPTY.getDefaultState() : fluidstate;
        }
    }

    public BlockState getBlockState() {
        return !this.valid ? Blocks.AIR.getDefaultState() : this.world.getBlockState(this.blockPos);
    }

    public void setAnglesInternal(float p_setAnglesInternal_1_, float p_setAnglesInternal_2_) {
        this.yaw = p_setAnglesInternal_1_;
        this.pitch = p_setAnglesInternal_2_;
    }

    public BlockState getBlockAtCamera() {
        if (!this.valid) {
            return Blocks.AIR.getDefaultState();
        } else {
            BlockState blockstate = this.world.getBlockState(this.blockPos);

            if (Reflector.IForgeBlockState_getStateAtViewpoint.exists()) {
                blockstate = (BlockState) Reflector.call(blockstate, Reflector.IForgeBlockState_getStateAtViewpoint, this.world, this.blockPos, this.pos);
            }

            return blockstate;
        }
    }

    public final Vector3f getViewVector() {
        return this.look;
    }

    public final Vector3f getUpVector() {
        return this.up;
    }

    public void clear() {
        this.world = null;
        this.renderViewEntity = null;
        this.valid = false;
    }
}

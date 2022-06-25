package de.dietrichpaul.winkel.feature.hack.engine.rotation;

import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.injection.accessor.client.IMinecraftClientMixin;
import de.dietrichpaul.winkel.property.PropertyMap;
import de.dietrichpaul.winkel.property.list.BooleanProperty;
import de.dietrichpaul.winkel.property.list.EnumProperty;
import de.dietrichpaul.winkel.property.list.IntegerProperty;
import de.dietrichpaul.winkel.util.math.MathUtil;
import de.dietrichpaul.winkel.util.raytrace.RayTraceUtil;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public abstract class SpoofAimbot extends AbstractRotationSpoof {

    public Random rotationRandom = new Random();

    private IntegerProperty minYawSpeedProperty = new IntegerProperty("Min yaw speed", "minYawSpeed", "", 10, 0, 180);
    private IntegerProperty maxYawSpeedProperty = new IntegerProperty("Max yaw speed", "maxYawSpeed", "", 20, 0, 180);
    private IntegerProperty minPitchSpeedProperty = new IntegerProperty("Min pitch speed", "minPitchSpeed", "", 10, 0, 180);
    private IntegerProperty maxPitchSpeedProperty = new IntegerProperty("Max pitch speed", "maxPitchSpeed", "", 20, 0, 180);

    private BooleanProperty decelerateProperty = new BooleanProperty("Decelerate", "decelerate", "", true);

    private EnumProperty<StrafeMode> strafeModeProperty = new EnumProperty<>("Strafe mode", "strafeMode", "", StrafeMode.SILENT, StrafeMode.values());
    private BooleanProperty lockViewProperty = new BooleanProperty("Lock view", "lockView", "", false);
    private EnumProperty<RayTraceMode> rayTraceModeProperty = new EnumProperty<>("Ray trace", "rayTrace", "", RayTraceMode.LEGIT, RayTraceMode.values());

    private boolean rotate;
    private boolean accelBack;
    private boolean confirmedClientRotation = true;

    public SpoofAimbot(String name, String description, HackCategory category) {
        super(name, description, category);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        if (!willBeReplaced() && !confirmedClientRotation) {
            float[] clientsideRotations = new float[2];
            clientsideRotations[0] = rotations[0];
            clientsideRotations[1] = rotations[1];

            float[] targetRotations = new float[2];
            targetRotations[0] = client.player.getYaw();
            targetRotations[1] = client.player.getPitch();
            MathUtil.fixSensitivity(((IMinecraftClientMixin) client).getFPS(), client.options.getMouseSensitivity().getValue(), targetRotations, clientsideRotations);

            client.player.setYaw(clientsideRotations[0]);
            client.player.setPitch(clientsideRotations[1]);
        }
    }

    @Override
    protected void makeProperties(PropertyMap map) {
        addProperty(map, this.strafeModeProperty);
        addProperty(map, this.lockViewProperty);
        addProperty(map, this.minYawSpeedProperty);
        addProperty(map, this.maxYawSpeedProperty);
        addProperty(map, this.minPitchSpeedProperty);
        addProperty(map, this.maxPitchSpeedProperty);
        addProperty(map, this.decelerateProperty);
        addProperty(map, this.rayTraceModeProperty);
        super.makeProperties(map);
    }

    @Override
    protected boolean isRotating() {
        return this.rotate || (willBeReplaced() ? this.accelBack : !this.confirmedClientRotation);
    }

    @Override
    public void tick(float[] previous, float[] decelerate, float[] rotations) {
        if (rotate) {
            float[] targetRotations = new float[2];
            targetRotations[0] = rotations[0];
            targetRotations[1] = rotations[1];
            tickEngine(previous, decelerate, targetRotations);
            this.confirmedClientRotation = false;

            filterRotations(previous, targetRotations, rotations);
        } else if (!this.lockViewProperty.getValue()) {
            if (decelerateProperty.getValue()) {
                float yawSpeed = MathUtil.nextFloat(rotationRandom, minYawSpeedProperty.getValue(), maxYawSpeedProperty.getValue());
                float pitchSpeed = MathUtil.nextFloat(rotationRandom, minPitchSpeedProperty.getValue(), maxPitchSpeedProperty.getValue());

                float deltaYaw = MathHelper.subtractAngles(previous[0], decelerate[0]);
                float deltaPitch = MathHelper.subtractAngles(previous[1], decelerate[1]);

                double deltaLength = Math.hypot(deltaPitch, deltaYaw);
                double speedLength = Math.hypot(pitchSpeed, yawSpeed);

                if (this.accelBack) {
                    this.accelBack = deltaLength > speedLength / 2 || Math.abs(deltaYaw) > yawSpeed || Math.abs(deltaPitch) > pitchSpeed;
                    if (this.accelBack) {
                        rotations[0] = previous[0] + MathHelper.clamp(deltaYaw, -yawSpeed, yawSpeed);
                        rotations[1] = previous[1] + MathHelper.clamp(deltaPitch, -pitchSpeed, pitchSpeed);
                    }
                    this.confirmedClientRotation = false;
                }
            }
            if (!this.accelBack && !this.confirmedClientRotation && !willBeReplaced()) {
                float[] clientsideRotations = new float[2];
                clientsideRotations[0] = rotations[0];
                clientsideRotations[1] = rotations[1];

                float[] targetRotations = new float[2];
                targetRotations[0] = client.player.getYaw();
                targetRotations[1] = client.player.getPitch();
                MathUtil.fixSensitivity(((IMinecraftClientMixin) client).getFPS(), client.options.getMouseSensitivity().getValue(), targetRotations, clientsideRotations);

                client.player.setYaw(clientsideRotations[0]);
                client.player.setPitch(clientsideRotations[1]);
                this.confirmedClientRotation = true;
            }
        } else {
            this.confirmedClientRotation = true;
            this.accelBack = false;
        }
    }

    protected float getCombatRange() {
        return 3F;
    }

    protected float getWorldRange() {
        return client.interactionManager.getReachDistance();
    }

    public abstract HitResult getTarget();

    @Override
    public HitResult rayTrace(float[] curr) {
        HitResult temp;
        return switch (rayTraceModeProperty.getValue()) {

            case LEGIT -> RayTraceUtil.rayTrace(client, curr, curr, getWorldRange(), getCombatRange(), 1F).collision();
            case FORCE_TARGET -> (temp = getTarget()) == null ? RayTraceUtil.rayTrace(client, curr, curr, getWorldRange(), getCombatRange(), 1F).collision() : temp;

        };
    }

    protected boolean getPickConditions() {
        return true;
    }

    @Override
    public StrafeMode getStrafeMode() {
        return this.strafeModeProperty.getValue();
    }

    @Override
    public boolean isViewLocked() {
        return this.lockViewProperty.getValue();
    }

    @Override
    public void preUpdate() {
        this.invalidateTarget();
        if (!this.decelerateProperty.getValue()) {
            this.accelBack = false;
        }
        if (getPickConditions()) {
            this.pickTarget();
        }
        this.rotate = this.hasTarget();
        if (this.rotate) {
            this.accelBack = true;
        }
    }

    public abstract boolean hasTarget();

    public abstract void invalidateTarget();

    public abstract void pickTarget();

    public abstract void tickEngine(float[] previous, float[] decelerate, float[] rotations);

    protected void filterRotationSpeed(float[] previous, float[] target, float[] out) {
        float yawSpeed = MathUtil.nextFloat(rotationRandom, minYawSpeedProperty.getValue(), maxYawSpeedProperty.getValue());
        float pitchSpeed = MathUtil.nextFloat(rotationRandom, minPitchSpeedProperty.getValue(), maxPitchSpeedProperty.getValue());

        float deltaYaw = MathHelper.subtractAngles(previous[0], target[0]);
        float deltaPitch = MathHelper.subtractAngles(previous[1], target[1]);

        out[0] = previous[0] + MathHelper.clamp(deltaYaw, -yawSpeed, yawSpeed);
        out[1] = previous[1] + MathHelper.clamp(deltaPitch, -pitchSpeed, pitchSpeed);
    }

    protected void filterRotationSensitivity(float[] previous, float[] target, float[] out) {
        out[0] = previous[0];
        out[1] = previous[1];

        MathUtil.fixSensitivity(((IMinecraftClientMixin) client).getFPS(), client.options.getMouseSensitivity().getValue(), target, out);
    }

    protected void filterRotations(float[] previous, float[] target, float[] out) {
        float[] tempRotations = new float[2];
        filterRotationSpeed(previous, target, tempRotations);
        filterRotationSensitivity(previous, tempRotations, out);
    }

}

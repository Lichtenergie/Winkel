package de.dietrichpaul.winkel.feature.hack.engine.rotation;

import de.dietrichpaul.winkel.event.list.CameraTransformRotationListener;
import de.dietrichpaul.winkel.event.list.FreezeRotationUpdateListener;
import de.dietrichpaul.winkel.event.list.network.MovementPacketListener;
import de.dietrichpaul.winkel.event.list.raytrace.PostRayTraceListener;
import de.dietrichpaul.winkel.event.list.raytrace.PreRayTraceListener;
import de.dietrichpaul.winkel.event.list.rotationrequest.ForceSpoofRotationListener;
import de.dietrichpaul.winkel.event.list.rotationrequest.RequestSpoofedPitchListener;
import de.dietrichpaul.winkel.event.list.rotationrequest.RequestSpoofedPrevPitchListener;
import de.dietrichpaul.winkel.event.list.rotationrequest.RequestSpoofedYawListener;
import de.dietrichpaul.winkel.event.list.tick.hud.PostTickHudListener;
import de.dietrichpaul.winkel.event.list.tick.move.JumpStrafeListener;
import de.dietrichpaul.winkel.event.list.tick.move.StrafeInputEmulationListener;
import de.dietrichpaul.winkel.event.list.tick.move.StrafeListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.injection.accessor.client.network.IClientPlayerEntityMixin;
import de.dietrichpaul.winkel.util.PathFixer;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.*;
import java.util.List;

public abstract class AbstractRotationSpoof extends Hack
        implements
        PostTickHudListener,
        PreRayTraceListener,
        MovementPacketListener,
        FreezeRotationUpdateListener,
        CameraTransformRotationListener,
        RequestSpoofedYawListener,
        RequestSpoofedPitchListener,
        RequestSpoofedPrevPitchListener,
        StrafeListener,
        JumpStrafeListener,
        StrafeInputEmulationListener,
        ForceSpoofRotationListener,
        PostRayTraceListener {

    private static final Comparator<AbstractRotationSpoof> ALTERNATIVE_PRIORITY = Comparator.comparingInt(AbstractRotationSpoof::supplyEventPriority);

    protected final float[] prevTickedRotations = new float[2];
    protected final float[] rotations = new float[2];

    private boolean rotating;
    private boolean lockView;
    private StrafeMode strafeMode;
    private int priority;

    public AbstractRotationSpoof(String name, String description, HackCategory category) {
        super(name, description, category);
    }

    @Override
    protected void onEnable() {
        startEngine();
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        stopEngine();
    }

    public void startEngine() {
        this.rotations[0] = ((IClientPlayerEntityMixin) client.player).getLastReportedYaw();
        this.rotations[1] = ((IClientPlayerEntityMixin) client.player).getLastReportedPitch();
        this.prevTickedRotations[0] = this.rotations[0];
        this.prevTickedRotations[1] = this.rotations[1];

        events.subscribe(PostTickHudListener.class, this);
        events.subscribe(StrafeListener.class, this, this::supplyEventPriority);
        events.subscribe(JumpStrafeListener.class, this, this::supplyEventPriority);
        events.subscribe(PreRayTraceListener.class, this, this::supplyEventPriority);
        events.subscribe(PostRayTraceListener.class, this, this::supplyEventPriority);
        events.subscribe(MovementPacketListener.class, this, this::supplyEventPriority);
        events.subscribe(RequestSpoofedYawListener.class, this, this::supplyEventPriority);
        events.subscribe(ForceSpoofRotationListener.class, this, this::supplyEventPriority);
        events.subscribe(RequestSpoofedPitchListener.class, this, this::supplyEventPriority);
        events.subscribe(FreezeRotationUpdateListener.class, this, this::supplyEventPriority);
        events.subscribe(StrafeInputEmulationListener.class, this, this::supplyEventPriority);
        events.subscribe(CameraTransformRotationListener.class, this, this::supplyEventPriority);
        events.subscribe(RequestSpoofedPrevPitchListener.class, this, this::supplyEventPriority);
    }

    public void stopEngine() {
        events.unsubscribe(StrafeListener.class, this);
        events.unsubscribe(JumpStrafeListener.class, this);
        events.unsubscribe(PostTickHudListener.class, this);
        events.unsubscribe(PreRayTraceListener.class, this);
        events.unsubscribe(PostRayTraceListener.class, this);
        events.unsubscribe(MovementPacketListener.class, this);
        events.unsubscribe(RequestSpoofedYawListener.class, this);
        events.unsubscribe(ForceSpoofRotationListener.class, this);
        events.unsubscribe(RequestSpoofedPitchListener.class, this);
        events.unsubscribe(FreezeRotationUpdateListener.class, this);
        events.unsubscribe(StrafeInputEmulationListener.class, this);
        events.unsubscribe(RequestSpoofedPrevPitchListener.class, this);
        events.unsubscribe(CameraTransformRotationListener.class, this);
    }

    @Override
    public void onCameraTransform(CameraTransformRotationEvent event) {
        event.stopHandling();
        if (this.rotating && this.lockView) {
            event.setInterpolatedYaw(MathHelper.lerpAngleDegrees(event.getTickDelta(), this.prevTickedRotations[0], this.rotations[0]));
            event.setInterpolatedPitch(MathHelper.lerpAngleDegrees(event.getTickDelta(), this.prevTickedRotations[1], this.rotations[1]));
            client.player.setYaw(event.getInterpolatedYaw());
            client.player.setPitch(event.getInterpolatedPitch());
        }
    }

    @Override
    public void onJumpStrafe(JumpStrafeEvent event) {
        event.stopHandling();
        if (this.rotating && this.strafeMode.fix) {
            event.setYaw(this.rotations[0]);
        }
    }

    @Override
    public void onStrafe(StrafeEvent event) {
        event.stopHandling();
        if (this.rotating && this.strafeMode.fix) {
            event.setYaw(this.rotations[0]);
        }
    }

    @Override
    public void onForceSpoofRotation(ForceSpoofRotationEvent event) {
        if (event.hasYaw()) this.rotations[0] = event.getYaw();
        if (event.hasPitch()) this.rotations[1] = event.getPitch();
    }

    @Override
    public void onFreezeRotationUpdate(FreezeRotationUpdateEvent event) {
        event.stopHandling();
        if (this.rotating && this.lockView) {
            event.setFrozen(true);
        }
    }

    @Override
    public final void onPreRayTrace() {
        if (client.player == null) return;
        this.lockView = isViewLocked();
        this.strafeMode = getStrafeMode();

        this.prevTickedRotations[0] = ((IClientPlayerEntityMixin) client.player).getLastReportedYaw();
        this.prevTickedRotations[1] = ((IClientPlayerEntityMixin) client.player).getLastReportedPitch();

        if (client.currentScreen != null)
            return;

        this.rotations[0] = client.player.getYaw();
        this.rotations[1] = client.player.getPitch();

        if (this.rotating) {
            float[] fallbackRotations = new float[]{
                    client.player.getYaw(),
                    client.player.getPitch()
            };
            float[] alternativeRotations = new float[]{
                    client.player.getYaw(),
                    client.player.getPitch()
            };

            List<AbstractRotationSpoof> alternativeSpoof = new LinkedList<>();
            for (Hack hack : winkel.getHackList().getHacks()) {
                if (hack instanceof AbstractRotationSpoof spoof && spoof.isEnabled()) {
                    alternativeSpoof.add(spoof);
                }
            }

            alternativeSpoof.sort(ALTERNATIVE_PRIORITY);

            Iterator<AbstractRotationSpoof> iterator = alternativeSpoof.iterator();

            while (iterator.hasNext()) {
                AbstractRotationSpoof spoof = iterator.next();
                if (spoof == this) continue;
                spoof.tick(this.prevTickedRotations, fallbackRotations, alternativeRotations);
                if (iterator.hasNext()) {
                    fallbackRotations[0] = alternativeRotations[0];
                    fallbackRotations[1] = alternativeRotations[1];
                }
            }

            tick(this.prevTickedRotations, alternativeRotations, this.rotations);
        }
    }

    @Override
    public void onRequestSpoofedYaw(RequestSpoofedYawEvent event) {
        event.stopHandling();
        if (this.rotating) {
            event.setYaw(this.rotations[0]);
        }
    }

    @Override
    public void onRequestSpoofedPitch(RequestSpoofedPitchEvent event) {
        event.stopHandling();
        if (this.rotating) {
            event.setPitch(this.rotations[1]);
        }
    }

    @Override
    public void onRequestSpoofedPrevPitch(RequestSpoofedPrevPitchEvent event) {
        event.stopHandling();
        if (this.rotating) {
            event.setPrevPitch(this.prevTickedRotations[1]);
        }
    }

    @Override
    public final void onPostTickHud() {
        if (client.player == null) return;
        this.preUpdate();
        this.priority = getPriority();
        this.rotating = isRotating();
    }

    @Override
    public void onPostRayTrace(PostRayTraceEvent event) {
        event.stopHandling();
        if (this.rotating) {
            client.crosshairTarget = rayTrace(this.rotations);
        }
    }

    @Override
    public void onStrafeInputEmulation(StrafeInputEmulationEvent event) {
        event.stopHandling();
        if (this.lockView || !this.rotating || !this.strafeMode.silentAngle) return;

        if (Math.abs(event.getMovementForward()) + Math.abs(event.getMovementSideways()) == 0) return;

        float angle = (float) (Math.toDegrees(Math.atan2(Math.signum(-event.getMovementSideways()), Math.signum(event.getMovementForward()))) + client.player.getYaw());
        if (event.getMovementForward() == 1 && event.getMovementSideways() == 0) angle = client.player.getYaw();

        if (this.strafeMode == StrafeMode.MOON_WALK) {
            double[] motions = new double[3];
            PathFixer.moveFlying(client, event.getMovementSideways() * 0.98F, event.getMovementForward() * 0.98F, client.player.getYaw(), motions);

            double x = client.player.getX() + motions[0];
            double z = client.player.getZ() + motions[2];
            Direction rotation = Direction.fromRotation(angle).getOpposite();
            BlockPos blockPos = new BlockPos(client.player.getPos());
            double aimDirection = -Math.toDegrees(Math.atan2(rotation.getOffsetX(), rotation.getOffsetZ()));
            int fortyFifthMove = ((int) Math.round(angle / 45F)) * 45;
            int fortyFifthAim = ((int) Math.round(aimDirection / 45F)) * 45;
            if ((fortyFifthMove == fortyFifthAim || Math.abs(fortyFifthAim - fortyFifthMove) % 180 == 0) && rotation.getOffsetY() == 0) {
                if (rotation.getOffsetX() == 0) {
                    x = blockPos.getX() + 0.5;
                }
                if (rotation.getOffsetZ() == 0) {
                    z = blockPos.getZ() + 0.5;
                }
            }

            double best = Double.MAX_VALUE;
            for (byte strafing = -1; strafing <= 1; strafing++) {
                for (byte forward = -1; forward <= 1; forward++) {

                    if (strafing == 0 && forward == 0) continue;

                    double[] bruteForceMotions = new double[3];
                    PathFixer.moveFlying(client, strafing * 0.98F, forward * 0.98F, this.rotations[0], bruteForceMotions);

                    double bruteX = client.player.getX() + bruteForceMotions[0];
                    double bruteZ = client.player.getZ() + bruteForceMotions[2];
                    double distance = Math.hypot(bruteX - x, bruteZ - z);

                    if (distance >= best)
                        continue;

                    best = distance;

                    event.setMovementSideways(strafing);
                    event.setMovementForward(forward);
                }
            }

            return;
        }


        double best = Double.MAX_VALUE;

        for (byte strafing = -1; strafing <= 1; strafing++) {
            for (byte forward = -1; forward <= 1; forward++) {

                if (strafing == 0 && forward == 0) continue;

                if (this.strafeMode.sprint && forward <= 0 && client.options.sprintKey.isPressed()) continue;

                float bruteforceAngle = (float) (Math.toDegrees(Math.atan2(Math.signum(-strafing), Math.signum(forward))) + this.rotations[0]);
                if (forward == 1 && strafing == 0) bruteforceAngle = this.rotations[0];

                float diff = MathHelper.angleBetween(angle, bruteforceAngle);

                if (diff >= best) continue;

                best = diff;
                event.setMovementSideways(strafing);
                event.setMovementForward(forward);
            }
        }
    }

    @Override
    public void onMovementPacket(MovementPacketEvent event) {
        event.stopHandling();
        event.setYaw(this.rotations[0]);
        event.setPitch(this.rotations[1]);
    }

    public void preUpdate() {
    }

    protected int supplyEventPriority() {
        if (!this.rotating) return Integer.MIN_VALUE;
        return this.priority;
    }

    protected boolean willBeReplaced() {
        for (Hack hack : winkel.getHackList().getHacks()) {
            if (hack instanceof AbstractRotationSpoof spoof) {
                if (spoof == this || !spoof.rotating) continue;

                return true;
            }
        }
        return false;
    }

    public abstract HitResult rayTrace(float[] curr);

    protected abstract boolean isRotating();

    public abstract void tick(float[] previous, float[] decelerate, float[] rotations);

    public abstract StrafeMode getStrafeMode();

    public abstract boolean isViewLocked();

    public abstract int getPriority();

}

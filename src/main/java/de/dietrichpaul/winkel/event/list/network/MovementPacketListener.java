package de.dietrichpaul.winkel.event.list.network;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface MovementPacketListener {

    void onMovementPacket(MovementPacketEvent event);

    class MovementPacketEvent extends BreakableAbstractEvent<MovementPacketListener> {

        private double x;
        private double y;
        private double z;

        private float yaw;
        private float pitch;

        private boolean onGround;

        private EventExecutor<MovementPacketListener> eventExecutor = listener -> listener.onMovementPacket(this);

        public MovementPacketEvent(double x, double y, double z, float yaw, float pitch, boolean onGround) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.onGround = onGround;
        }

        @Override
        public EventExecutor<MovementPacketListener> getEventExecutor() {
            return eventExecutor;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public float getYaw() {
            return yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public boolean isOnGround() {
            return onGround;
        }

        public void setOnGround(boolean onGround) {
            this.onGround = onGround;
        }

        @Override
        public Class<MovementPacketListener> getListenerType() {
            return MovementPacketListener.class;
        }

    }

}
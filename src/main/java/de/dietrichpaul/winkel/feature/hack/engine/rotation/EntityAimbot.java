package de.dietrichpaul.winkel.feature.hack.engine.rotation;

import de.dietrichpaul.winkel.event.list.ExtensionPickTargetListener;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.property.PropertyMap;
import de.dietrichpaul.winkel.property.list.EnumProperty;
import de.dietrichpaul.winkel.property.list.IntegerProperty;
import de.dietrichpaul.winkel.property.list.target.TargetSelectionProperty;
import de.dietrichpaul.winkel.util.EnumIdentifiable;
import de.dietrichpaul.winkel.util.MathUtil;
import de.dietrichpaul.winkel.util.priority.killaura.KillAuraPriorityMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public abstract class EntityAimbot extends SpoofAimbot implements ExtensionPickTargetListener {

    public EntityAimbot(String name, String description, HackCategory category) {
        super(name, description, category);
    }

    protected TargetSelectionProperty targetSelectionProperty = new TargetSelectionProperty("Targets", "targets", "", new ArrayList<>());
    private IntegerProperty fovProperty = new IntegerProperty("FOV", "fov", "", 360, 0, 360);
    private EnumProperty<KillAuraPriorityMode> priorityProperty = new EnumProperty<>("Priority", "priority", "", KillAuraPriorityMode.DIRECTION, KillAuraPriorityMode.values());
    private EnumProperty<TargetMode> targetModeProperty = new EnumProperty<>("Target mode", "targetMode", "", TargetMode.SINGLE, TargetMode.values());

    private IntegerProperty switchTicksProperty = new IntegerProperty("Switch ticks", "switchTicks", "", 0, 0, 60);

    private int switchDelay;

    protected Entity target;
    protected Entity prevTarget;

    @Override
    protected void onEnable() {
        switchDelay = 0;
        super.onEnable();
    }

    @Override
    protected void makeProperties(PropertyMap map) {
        addProperty(map, this.targetSelectionProperty);
        addProperty(map, this.fovProperty);
        addProperty(map, this.priorityProperty);
        addProperty(map, this.targetModeProperty);
        addProperty(map, this.switchTicksProperty);
        super.makeProperties(map);
    }

    @Override
    public boolean hasTarget() {
        return this.target != null;
    }

    @Override
    public void invalidateTarget() {
        this.target = null;
    }

    private boolean fovFilter(Entity entity) {
        Vec3d camera = client.player.getCameraPosVec(1.0F);
        float[] rotations = new float[2];
        MathUtil.getRotations(rotations, camera, entity.getBoundingBox().getCenter());
        float deltaYaw = MathHelper.angleBetween(client.player.getYaw(), rotations[0]);
        return 2 * deltaYaw <= fovProperty.getValue();
    }

    protected boolean filter(Entity entity) {
        return true;
    }

    private boolean isTargetOk(Entity entity) {
        return this.targetSelectionProperty.predicate(client).test(entity) && this.filter(entity) && this.fovFilter(entity);
    }

    private void selectTarget() {
        double bestWeight = Double.MAX_VALUE;
        for (Entity entity : client.world.getEntities()) {
            if (!this.isTargetOk(entity))
                continue;

            double weight = this.priorityProperty.getValue().getTargetSortingAlgorithm().getWeight(client, entity);
            if (weight >= bestWeight)
                continue;

            this.target = entity;
            bestWeight = weight;
        }
        this.switchDelay = this.switchTicksProperty.getValue() + 1;
    }

    @Override
    public void pickTarget() {
        if (this.switchDelay > 0)
            this.switchDelay--;
        if (this.targetModeProperty.getValue() == TargetMode.SINGLE) { // single target
            if (isTargetOk(this.prevTarget)) {
                this.target = prevTarget;
            } else {
                this.selectTarget();
            }
        } else { // switch target
            if (isTargetOk(this.prevTarget) && switchDelay != 0) {
                this.target = prevTarget;
            } else {
                this.selectTarget();
            }
        }

        if (this.target != null)
            this.prevTarget = target;
    }

    @Override
    public void onWTapPickTarget(ExtensionPickTargetEvent event) {
        event.stopHandling();
        if (this.target instanceof LivingEntity living)
            event.setTarget(living);
    }

    enum TargetMode implements EnumIdentifiable {

        SINGLE("Single", "single"), SWITCH("Switch", "switch");

        private String display;
        private String lowerCamelCase;

        TargetMode(String display, String lowerCamelCase) {
            this.display = display;
            this.lowerCamelCase = lowerCamelCase;
        }

        @Override
        public String getDisplay() {
            return display;
        }

        @Override
        public String getLowerCamelCase() {
            return lowerCamelCase;
        }

    }

}

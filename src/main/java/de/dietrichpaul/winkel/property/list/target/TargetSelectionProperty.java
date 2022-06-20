package de.dietrichpaul.winkel.property.list.target;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.arguments.target.TargetSelectionAddArgumentType;
import de.dietrichpaul.winkel.feature.command.arguments.target.TargetSelectionRemoveArgumentType;
import de.dietrichpaul.winkel.feature.command.node.SimpleArgumentBuilder;
import de.dietrichpaul.winkel.feature.gui.tab.Item;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Checkbox;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Container;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Radio;
import de.dietrichpaul.winkel.property.AbstractProperty;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TargetSelectionProperty extends AbstractProperty<List<TargetFilter>> {

    public static final Map<String, TargetFilter> targetFilters = new HashMap<>();

    private static void addFilter(TargetFilter filter) {
        targetFilters.put(filter.lowerCamelCase(), filter);
    }

    static {
        addFilter(new TargetFilter("Players", "players", "Would select players", (mc, e) -> !(e instanceof OtherClientPlayerEntity)));
        addFilter(new TargetFilter("Sleeping", "sleeping", "Would select players", (mc, e) -> !(e instanceof PlayerEntity && ((PlayerEntity) e).isSleeping())));
        addFilter(new TargetFilter("Monster", "monster", "Would select players", (mc, e) -> !(e instanceof Monster)));
        addFilter(new TargetFilter("Pigmen", "pigmen", "Would select players", (mc, e) -> !(e instanceof ZombifiedPiglinEntity)));
        addFilter(new TargetFilter("Endermen", "endermen", "Would select players", (mc, e) -> !(e instanceof EndermanEntity)));
        addFilter(new TargetFilter("Animals", "animals", "Would select players", (mc, e) -> !(e instanceof AnimalEntity || e instanceof AmbientEntity || e instanceof WaterCreatureEntity)));
        addFilter(new TargetFilter("Babies", "babies", "Would select players", (mc, e) -> !(e instanceof PassiveEntity && ((PassiveEntity) e).isBaby())));
        addFilter(new TargetFilter("Traders", "traders", "Would select players", (mc, e) -> !(e instanceof MerchantEntity)));
        addFilter(new TargetFilter("Golems", "golems", "Would select players", (mc, e) -> !(e instanceof GolemEntity)));
        addFilter(new TargetFilter("Invisible", "invisible", "Would select players", (mc, e) -> !e.isInvisible()));
        addFilter(new TargetFilter("Custom Named", "customNamed", "Would select players", (mc, e) -> !e.hasCustomName()));
        addFilter(new TargetFilter("Armor Stands", "armorStands", "Would select players", (mc, e) -> !(e instanceof ArmorStandEntity)));
        addFilter(new TargetFilter("EndCrystals", "endCrystals", "Would select players", (mc, e) -> !(e instanceof EndCrystalEntity)));
        addFilter(new TargetFilter("Fireballs", "fireballs", "Would select players", (mc, e) -> !(e instanceof FireballEntity)));
        addFilter(new TargetFilter("Friends", "friends", "Would select players", (mc, e) -> {
            if (e instanceof OtherClientPlayerEntity) {
                return !WinkelClient.INSTANCE.getFriendManager().isFriend(e);
            }
            return true;
        }));
        addFilter(new TargetFilter("Team mates", "teamMates", "Would select players", (mc, e) -> {
            PlayerListEntry myTab = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (myTab != null) {
                Team scoreboardTeam = myTab.getScoreboardTeam();
                if (scoreboardTeam != null) {
                    Text prefix = scoreboardTeam.getPrefix();
                    if (prefix != null) {
                        if (!(e instanceof OtherClientPlayerEntity))
                            return true;
                        PlayerListEntry otherTab = mc.getNetworkHandler().getPlayerListEntry(e.getUuid());
                        if (otherTab == null)
                            return true;
                        if (otherTab.getScoreboardTeam() == null)
                            return true;
                        if (otherTab.getScoreboardTeam().getPrefix() == null)
                            return false;
                        return !otherTab.getScoreboardTeam().getPrefix().equals(prefix);
                    }
                }
            }
            return true;
        }));
    }

    public TargetSelectionProperty(String name, String lowerCamelCase, String description, List<TargetFilter> value) {
        super(name, lowerCamelCase, description, value);
    }

    @Override
    public void parse(String literal) {
    }

    @Override
    public void makeCommand(SimpleArgumentBuilder<InternalCommandSource, ?> builder) {
         builder
                .then(
                        Command.literal("add")
                                .then(Command.argument("filter", TargetSelectionAddArgumentType.addTargetSelection(this)).executes(context -> {
                                    TargetFilter filter = TargetSelectionAddArgumentType.getTargetFilter(context, "filter");
                                    getValue().add(filter);
                                    WinkelClient.INSTANCE.getChat().print("command.property.added", Text.literal(filter.name()).formatted(Formatting.GRAY),
                                            Text.literal(getName()).formatted(Formatting.GRAY), Text.literal(getParent().asString()).formatted(Formatting.GRAY));
                                    return 1;
                                }))
                )
                .then(
                        Command.literal("remove")
                                .then(Command.argument("filter", TargetSelectionRemoveArgumentType.removeTargetSelection(this)).executes(context -> {
                                    TargetFilter filter = TargetSelectionRemoveArgumentType.getTargetFilter(context, "filter");
                                    getValue().remove(filter);
                                    WinkelClient.INSTANCE.getChat().print("command.property.removed", Text.literal(filter.name()).formatted(Formatting.GRAY),
                                            Text.literal(getName()).formatted(Formatting.GRAY), Text.literal(getParent().asString()).formatted(Formatting.GRAY));
                                    return 1;
                                }))
                );
    }

    @Override
    public void readFromJson(JsonObject element) {
        List<TargetFilter> value = new ArrayList<>();
        for (JsonElement filter : element.getAsJsonArray("enabledFilters")) {
            getValue().add(targetFilters.get(filter.getAsString()));
        }
    }

    @Override
    public void writeToJson(JsonObject element) {
        JsonArray enabledFilters = new JsonArray();
        for (TargetFilter targetFilter : getValue()) {
            enabledFilters.add(targetFilter.lowerCamelCase());
        }
        element.add("enabledFilters", enabledFilters);
    }

    public <E extends Entity> Predicate<E> predicate(MinecraftClient client) {
        Predicate<E> predicate = Objects::nonNull;
        predicate = predicate.and(e -> e != client.player && !e.isRemoved());
        predicate = predicate.and(e -> e instanceof LivingEntity && ((LivingEntity) e).getHealth() > 0 || (e instanceof EndCrystalEntity || e instanceof FireballEntity));

        List<TargetFilter> ignoredEntityFilter = new ArrayList<>(targetFilters.values());
        for (TargetFilter filter : this.getValue()) {
            ignoredEntityFilter.remove(filter);
        }

        for (TargetFilter removeFilter : ignoredEntityFilter) {
            predicate = predicate.and(e -> removeFilter.filter().test(client, e));
        }

        return predicate;
    }

    public <E extends Entity> Stream<E> filter(MinecraftClient client, Stream<E> stream) {
        return stream.filter(predicate(client));
    }

    @Override
    public Item createTabGuiItem() {
        Container container = new Container(() -> Text.literal(this.getName()));
        for (TargetFilter filter : targetFilters.values()) {
            container.add(new Checkbox(() -> {
                return Text.literal(filter.name());
            }, () -> {
                return this.getValue().contains(filter);
            }, () -> {
                if (this.getValue().contains(filter)) {
                    this.getValue().remove(filter);
                } else {
                    this.getValue().add(filter);
                }
            }));
        }

        return container;
    }

}

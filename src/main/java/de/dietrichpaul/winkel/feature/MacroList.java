package de.dietrichpaul.winkel.feature;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.KeyInputListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MacroList implements KeyInputListener {
    private final Map<String, List<String>> actions = new LinkedHashMap<>();

    public MacroList() {
        WinkelClient.INSTANCE.getEventDispatcher().subscribe(KeyInputListener.class, this);
    }

    public void bind(String key, String action) {
        this.actions.computeIfAbsent(key, k -> new LinkedList<>()).add(action);
        WinkelClient.INSTANCE.getConfigManager().macro.save();
    }

    public void unbind(String key, int index) {
        this.actions.get(key).remove(index);
    }

    public void unbind(String key) {
        this.actions.get(key).remove(key);
    }

    @Override
    public void onInput(KeyInputEvent event) {
        if (event.getAction() != GLFW.GLFW_PRESS || MinecraftClient.getInstance().currentScreen != null)
            return;
        List<String> actions = this.actions.get(WinkelClient.INSTANCE.getKeyboardMapper().fromCode(event.getKey()));
        if (actions == null)
            return;
        for (String action : actions) {
            Hack hack = WinkelClient.INSTANCE.getHackList().getHack(action);
            if (hack != null) {
                hack.toggle();
            }
        }
    }

    public void remove(String name) {
        this.actions.remove(name);
    }

    public List<String> getActions(String name) {
        return this.actions.get(name);
    }

    public Map<String, List<String>> getActions() {
        return actions;
    }

}

package de.dietrichpaul.winkel.util.keyboard;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Language;
import org.lwjgl.glfw.GLFW;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class KeyboardMapper {

    private static final Gson GSON = new Gson();
    private static final Pattern TOKEN_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
    private final BiMap<Integer, String> keyNameMap = HashBiMap.create();
    private BiMap<String, Integer> nameKeyMap;

    public void start() {
        Map<String, String> fallbackMappings = new HashMap<>();
        try {
            JsonObject jsonObject = GSON.fromJson(new InputStreamReader(Language.class.getResourceAsStream("/assets/minecraft/lang/en_us.json"), StandardCharsets.UTF_8), JsonObject.class);

            for (Map.Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
                String string = TOKEN_PATTERN.matcher(JsonHelper.asString(stringJsonElementEntry.getValue(), stringJsonElementEntry.getKey())).replaceAll("%$1s");
                fallbackMappings.put(stringJsonElementEntry.getKey(), string);
            }
        } catch (JsonParseException ignored) {
        }
        for (Field field : GLFW.class.getFields()) {
            if (!field.getName().startsWith("GLFW_KEY_"))
                continue;
            try {
                int id = field.getInt(null);
                if (id == -1) continue;
                int scan = GLFW.glfwGetKeyScancode(id);
                if (id == GLFW.GLFW_KEY_UNKNOWN)
                    continue;
                String name = GLFW.glfwGetKeyName(id, scan);
                if (name == null) {
                    name = fallbackMappings.get(InputUtil.fromKeyCode(id, scan).getTranslationKey());
                    if (name == null)
                        continue;
                }
                name = name.replace(" ", "_").toLowerCase(Locale.ROOT);
                if (!keyNameMap.containsValue(name))
                    keyNameMap.put(id, name);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        this.nameKeyMap = keyNameMap.inverse();
    }

    public int fromName(String name) {
        return nameKeyMap.get(name);
    }

    public String fromCode(int code) {
        return keyNameMap.get(code);
    }

    public BiMap<Integer, String> getKeyNameMap() {
        return keyNameMap;
    }

    public BiMap<String, Integer> getNameKeyMap() {
        return nameKeyMap;
    }

}

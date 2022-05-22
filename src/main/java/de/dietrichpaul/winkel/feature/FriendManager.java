package de.dietrichpaul.winkel.feature;

import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.TreeMap;

public class FriendManager {
    private final Map<String, String> friendTags = new TreeMap<>(String::compareToIgnoreCase);

    public void addFriend(String name, String tag) {
        this.friendTags.put(name, tag);
    }

    public void addFriend(String name) {
        addFriend(name, name);
    }

    public void removeFriend(String name) {
        this.friendTags.remove(name);
    }

    public boolean isFriend(String name) {
        return this.friendTags.containsKey(name);
    }

    public boolean isFriend(Entity entity) {
        if (entity instanceof OtherClientPlayerEntity player) {
            return isFriend(player.getGameProfile().getName());
        }
        return false;
    }

    public Map<String, String> getFriendTags() {
        return friendTags;
    }
}

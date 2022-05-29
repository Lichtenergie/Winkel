package de.dietrichpaul.winkel.util.keyboard;

public class WKey {

    private int keyCode;
    private String name;

    public WKey(int keyCode, String name) {
        this.keyCode = keyCode;
        this.name = name;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public String getName() {
        return name;
    }

}

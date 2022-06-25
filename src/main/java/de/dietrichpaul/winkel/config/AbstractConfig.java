package de.dietrichpaul.winkel.config;

import de.dietrichpaul.winkel.WinkelClient;

import java.io.File;
import java.io.IOException;

public abstract class AbstractConfig {

    private String name;
    private File file;
    private boolean loaded;
    private ConfigType type;

    protected WinkelClient winkel = WinkelClient.INSTANCE;

    public AbstractConfig(String name, ConfigType type) {
        this.name = name;
        this.type = type;
        this.file = new File(winkel.getDirectory(), this.name);
    }

    public ConfigType getType() {
        return type;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public void load() throws IOException {
        read();
        save();
    }

    public void save() {
        if (!isLoaded())
            return;
        System.out.println("SAVED " + " " + this.name);
        try {
            this.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void markAsLoaded() {
        this.loaded = true;
    }

    protected abstract void read() throws IOException;

    protected abstract void write() throws IOException;

}

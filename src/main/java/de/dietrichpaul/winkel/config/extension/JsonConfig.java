package de.dietrichpaul.winkel.config.extension;

import com.google.gson.JsonElement;
import de.dietrichpaul.winkel.config.AbstractConfig;
import de.dietrichpaul.winkel.config.ConfigType;
import de.dietrichpaul.winkel.util.IOUtil;

import java.io.IOException;

public abstract class JsonConfig<T extends JsonElement> extends AbstractConfig {

    public JsonConfig(String name, ConfigType type) {
        super(name, type);
    }

    @Override
    protected void read() throws IOException {
        this.read(IOUtil.readGsonOr(getFile(), make()));
        this.markAsLoaded();
    }

    @Override
    protected void write() throws IOException {
        T elem = make();
        write(elem);
        IOUtil.writePrettyGson(getFile(), elem);
    }

    protected abstract T make();

    protected abstract void read(T element);

    protected abstract void write(T element);

}

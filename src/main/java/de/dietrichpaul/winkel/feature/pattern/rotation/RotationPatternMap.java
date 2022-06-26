package de.dietrichpaul.winkel.feature.pattern.rotation;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.pattern.click.ClickPattern;
import de.dietrichpaul.winkel.feature.pattern.click.impl.FileClickPattern;
import de.dietrichpaul.winkel.feature.pattern.rotation.impl.CenterPattern;
import de.dietrichpaul.winkel.feature.pattern.rotation.impl.HeadPattern;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RotationPatternMap {
    private final Map<String, RotationPattern> map = new LinkedHashMap<>();

    private final File directory;

    public RotationPatternMap() {
        this.directory = new File(WinkelClient.INSTANCE.getDirectory(), "patterns" + File.separator + "rotation");
        if (!this.directory.exists())
            this.directory.mkdirs();

        addPattern(new CenterPattern());
        addPattern(new HeadPattern());
    }

    public void addPattern(RotationPattern pattern) {
        this.map.put(pattern.asString(), pattern);
    }

    public File getDirectory() {
        return directory;
    }

    public Map<String, RotationPattern> getMap() {
        return map;
    }

}

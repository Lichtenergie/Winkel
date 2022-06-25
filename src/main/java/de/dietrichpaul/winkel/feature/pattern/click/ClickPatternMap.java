package de.dietrichpaul.winkel.feature.pattern.click;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.pattern.click.impl.FileClickPattern;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class ClickPatternMap {

    private final Map<String, ClickPattern> map = new LinkedHashMap<>();

    private final File directory;

    public ClickPatternMap() {
        this.directory = new File(WinkelClient.INSTANCE.getDirectory(), "patterns" + File.separator + "click");
        if (!this.directory.exists())
            this.directory.mkdirs();

        for (File file : this.directory.listFiles()) {
            String baseName = FilenameUtils.getBaseName(file.getName());
            try {
                List<int[]> lines = new LinkedList<>();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while (true) {
                    String line = reader.readLine();
                    if (line == null || line.startsWith("-"))
                        break;
                    String[] strArr = line.substring(1, line.indexOf("]")).split(", ");
                    int[] arr = new int[20];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = Integer.parseInt(strArr[i]);
                    }
                    lines.add(arr);
                }
                reader.close();
                addPattern(baseName, new FileClickPattern(baseName, lines));
            } catch (Exception ignored) {
            }
        }
    }

    public void addPattern(String name, ClickPattern pattern) {
        this.map.put(name, pattern);
    }

    public File getDirectory() {
        return directory;
    }

    public Map<String, ClickPattern> getMap() {
        return map;
    }

}

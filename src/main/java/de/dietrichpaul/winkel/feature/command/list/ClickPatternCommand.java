package de.dietrichpaul.winkel.feature.command.list;

import de.dietrichpaul.winkel.event.list.DoAttackListener;
import de.dietrichpaul.winkel.event.list.tick.hud.PostTickHudListener;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.arguments.FileArgumentType;
import de.dietrichpaul.winkel.feature.command.node.SimpleBaseArgumentBuilder;
import de.dietrichpaul.winkel.util.math.MathUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ClickPatternCommand extends Command implements PostTickHudListener, DoAttackListener {

    private File file;

    private boolean recording;
    private long started;

    private int[] zeile = new int[20];
    private int spalte;

    private final List<int[]> matrix = new LinkedList<>();

    public ClickPatternCommand() {
        super("clickpattern");
    }

    @Override
    public void build(SimpleBaseArgumentBuilder<InternalCommandSource> base) {
        base.then(literal("start")
                .requires(src -> !this.recording)
                .then(argument("out", FileArgumentType.file(winkel.getClickPatternMap().getDirectory(), "txt", true, true))
                        .executes(ctx -> {
                            this.matrix.clear();
                            this.zeile = new int[20];
                            this.spalte = 0;
                            this.recording = true;
                            events.subscribe(PostTickHudListener.class, this);
                            events.subscribe(DoAttackListener.class, this);
                            chat.print("command.clickpattern.start");
                            this.started = System.currentTimeMillis();
                            this.file = FileArgumentType.getFile("out", ctx);
                        }))
        );
        base.then(literal("stop")
                .requires(src -> this.recording)
                .executes(() -> {
                    this.recording = false;
                    events.unsubscribe(PostTickHudListener.class, this);
                    events.unsubscribe(DoAttackListener.class, this);
                    long time = System.currentTimeMillis() - this.started;
                    chat.print("command.clickpattern.stop", String.format("%.2f", time / 1000F));

                    CompletableFuture.runAsync(() -> {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
                            int avg = 0;
                            for (int[] zeile : this.matrix) {
                                writer.write(Arrays.toString(zeile));
                                writer.write("\t");
                                int sum = MathUtil.sum(zeile);
                                writer.write(String.valueOf(sum));
                                avg += sum;
                                writer.newLine();
                            }
                            writer.write("-".repeat(60));
                            writer.newLine();
                            writer.write(String.valueOf(avg / (double) this.matrix.size()));
                            writer.flush();
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }));
    }

    @Override
    public void onDoAttack() {
        this.zeile[this.spalte]++;
    }

    @Override
    public void onPostTickHud() {
        this.spalte++;
        if (this.spalte == 20) {
            this.spalte = 0;
            this.matrix.add(this.zeile);
            this.zeile = new int[20];
        }
    }

}

package de.dietrichpaul.winkel.feature.hack.impl.combat;

import de.dietrichpaul.winkel.event.list.render.RenderOverlayListener;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.feature.hack.engine.click.ClickCallback;
import de.dietrichpaul.winkel.feature.hack.engine.click.InputHandler;
import de.dietrichpaul.winkel.feature.pattern.click.ClickPattern;
import de.dietrichpaul.winkel.feature.pattern.click.impl.CustomClickPattern;
import de.dietrichpaul.winkel.property.AbstractProperty;
import de.dietrichpaul.winkel.property.PropertyMap;
import de.dietrichpaul.winkel.property.list.BooleanProperty;
import de.dietrichpaul.winkel.property.list.IntegerProperty;
import de.dietrichpaul.winkel.property.list.ModeProperty;
import de.dietrichpaul.winkel.util.ArrayUtil;
import de.dietrichpaul.winkel.util.math.MathUtil;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.ArrayUtils;

import java.security.SecureRandom;
import java.text.DecimalFormat;

public class KillauraHack extends AimbotHack implements InputHandler, RenderOverlayListener {

    // [zeilen][spalten]
    private int[][] clickMatrix = null;
    private int spalte;
    private int zeile;

    private IntegerProperty minCPS = new IntegerProperty("MinCPS", "minCPS", "", 7, 0, 80);
    private IntegerProperty maxCPS = new IntegerProperty("MaxCPS", "maxCPS", "", 15, 0, 80);

    private SecureRandom clickRandom;
    private DecimalFormat matrixFormat = new DecimalFormat("00");

    private ModeProperty<ClickPattern> clickPattern = new ModeProperty<>("ClickPattern", "clickPattern", "", 0,
            ArrayUtil.addToNew(winkel.getClickPatternMap().getMap().values(), new CustomClickPattern("Custom", maxCPS::getValue, minCPS::getValue))) {
        @Override
        public void setValue(ClickPattern value) {
            super.setValue(value);
            clickMatrix = null;
        }
    };

    private BooleanProperty debug = new BooleanProperty("Debug", "debug", "", false);
    private BooleanProperty legacyClicking = new BooleanProperty("LegacyClicking", "legacyClicking", "", false);

    public KillauraHack() {
        super("Killaura", "", HackCategory.COMBAT);
    }

    @Override
    protected void makeProperties(PropertyMap map) {
        addProperty(map, this.clickPattern);
        addProperty(map, this.minCPS);
        addProperty(map, this.maxCPS);
        addProperty(map, this.legacyClicking);
        super.makeProperties(map);
        addProperty(map, this.debug);
    }

    @Override
    protected void onEnable() {
        clickMatrix = null;
        events.subscribe(RenderOverlayListener.class, this, Integer.MAX_VALUE);
        this.clickRandom = new SecureRandom();
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        events.unsubscribe(RenderOverlayListener.class, this);
        super.onDisable();
    }

    @Override
    public boolean canClick() {
        return this.hasTarget();
    }

    @Override
    public int getClickingPriority() {
        return 0;
    }

    @Override
    protected void postTickEngine() {
        if (clickMatrix == null || zeile == clickMatrix.length || (spalte == clickMatrix[zeile].length - 1 && zeile == clickMatrix.length - 1)) {
            int[] cps = new int[40];
            for (int i = 0; i < cps.length; i++) {
                cps[i] = MathUtil.nextInt(this.clickRandom, 5, 18);
            }
            clickMatrix = this.clickPattern.getValue().generateMatrix(this.clickRandom);
            zeile = 0;
            spalte = 0;

        } else if (spalte == clickMatrix[zeile].length - 1) {
            zeile++;
            spalte = 0;
        } else {
            spalte++;
        }
    }

    @Override
    public void click(ClickCallback callback) {
        if (client.player.getAttackCooldownProgress(0) < 1.0F && !this.legacyClicking.getValue()) {
            return;
        }
        if (client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            if (client.player.isCreative())
                return;
            BlockPos pos = ((BlockHitResult) client.crosshairTarget).getBlockPos();

            if (client.world.getBlockState(pos).calcBlockBreakingDelta(client.player, client.world, pos) >= 1F)
                return;
        }
        callback.pressAttack(this.clickMatrix[this.zeile][this.spalte]);
    }

    @Override
    public void onRender(RenderOverlayEvent event) {
        if (!this.debug.getValue() || this.clickMatrix == null)
            return;
        String[] lines = new String[this.clickMatrix.length];
        double avg = 0.0;
        for (int i = 0; i < this.clickMatrix.length; i++) {
            double cps = MathUtil.getMedian(this.clickMatrix[i]) * 20;
            avg += cps;
            lines[i] = Formatting.YELLOW + matrixFormat.format(cps) + " - " + Formatting.RED;
            for (int j = 0; j < this.clickMatrix[i].length; j++) {
                if (this.zeile == i && this.spalte == j) {
                    lines[i] += Formatting.WHITE;
                    lines[i] += Formatting.BOLD;
                } else if (this.zeile == i && this.spalte == j - 1) {
                    lines[i] += Formatting.RED;
                }
                lines[i] += this.clickMatrix[i][j];
            }
        }
        int maxWidth = 0;
        for (String l : lines) {
            maxWidth = Math.max(maxWidth, client.textRenderer.getWidth(l));
        }

        avg /= this.clickMatrix.length;

        for (int i = 0; i < lines.length; i++) {
            client.textRenderer.draw(event.getMatrices(), lines[i], client.getWindow().getScaledWidth() / 3 - maxWidth / 2, 5 + i * client.textRenderer.fontHeight, -1);
        }
        DrawableHelper.fill(event.getMatrices(), client.getWindow().getScaledWidth() / 3 - maxWidth / 2, (this.clickMatrix.length) * client.textRenderer.fontHeight + 6, client.getWindow().getScaledWidth() / 3 + maxWidth / 2, this.clickMatrix.length * client.textRenderer.fontHeight + 7, -1);
        client.textRenderer.draw(event.getMatrices(), Formatting.GOLD + String.valueOf(avg), client.getWindow().getScaledWidth() / 3 - maxWidth / 2, 5 + this.clickMatrix.length * client.textRenderer.fontHeight + 4, -1);
    }

}

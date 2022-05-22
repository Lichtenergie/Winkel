package de.dietrichpaul.winkel.feature.hack.visual;

import de.dietrichpaul.winkel.event.list.render.RenderOverlayListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import net.minecraft.client.font.TextRenderer;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class HudHack extends Hack implements RenderOverlayListener {

    public HudHack() {
        super("Hud", "", HackCategory.VISUAL);
    }

    @Override
    protected void onEnable() {
        winkel.getEventDispatcher().subscribe(RenderOverlayListener.class, this);
    }

    @Override
    protected void onDisable() {
        winkel.getEventDispatcher().unsubscribe(RenderOverlayListener.class, this);
    }

    @Override
    public void onRender(RenderOverlayEvent event) {
        TextRenderer font = client.textRenderer;
        List<Hack> sortedHacks = new LinkedList<>();
        for (Hack hack : winkel.getHackList().getHacks()) {
            if (hack.isEnabled())
                sortedHacks.add(hack);
        }
        sortedHacks.sort(Comparator.comparingInt(hack -> -font.getWidth(hack.getName())));

        int yOffset = 0;
        int windowWidth = client.getWindow().getScaledWidth();
        for (Hack hack : sortedHacks) {
            int width = font.getWidth(hack.getName());
            // DrawableHelper.fill(event.getMatrices(), windowWidth - width - 8, yOffset, windowWidth, yOffset + 14, Integer.MIN_VALUE);
            font.drawWithShadow(event.getMatrices(), hack.getName(), windowWidth - width - 4, yOffset + 4, -1);
            yOffset += 14;
        }
    }

}

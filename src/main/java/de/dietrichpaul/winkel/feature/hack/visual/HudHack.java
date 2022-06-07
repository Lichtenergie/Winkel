package de.dietrichpaul.winkel.feature.hack.visual;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.KeyInputListener;
import de.dietrichpaul.winkel.event.list.render.RenderOverlayListener;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Button;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Container;
import de.dietrichpaul.winkel.feature.gui.tab.TabGui;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class HudHack extends Hack implements RenderOverlayListener {

    private final Identifier logo;

    private TabGui tabGui;

    public HudHack() {
        super("Hud", "", HackCategory.VISUAL);
        logo = new Identifier(WinkelClient.NAME.toLowerCase(), "icons/client.png");


    }

    @Override
    protected void onEnable() {
        if (this.tabGui == null) {
            this.tabGui = new TabGui();
            for (HackCategory category : HackCategory.values()) {
                Container container = new Container(category::getDisplay);
                for (Hack hack : WinkelClient.INSTANCE.getHackList().getHacks()) {
                    if (hack.getCategory() == category) {
                        container.add(new Button(hack::getButtonText, hack::toggle));
                    }
                }
                this.tabGui.add(container);
            }
        }
        winkel.getEventDispatcher().subscribe(RenderOverlayListener.class, this);
        winkel.getEventDispatcher().subscribe(KeyInputListener.class, this.tabGui);
    }

    @Override
    protected void onDisable() {
        winkel.getEventDispatcher().unsubscribe(RenderOverlayListener.class, this);
        winkel.getEventDispatcher().unsubscribe(KeyInputListener.class, this.tabGui);
    }

    // todo: widget-system
    @Override
    public void onRender(RenderOverlayEvent event) {
        TextRenderer font = client.textRenderer;

        final int size = 32;
        RenderSystem.setShaderTexture(0, logo);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        DrawableHelper.drawTexture(event.getMatrices(), 2, 2, 0, 0,
                size, size, size, size);

        // client-name
        final float titleScale = 1.65F, versionScale = .75F;
        event.getMatrices().push();
        event.getMatrices().translate(size + 4, 0, 0);
        event.getMatrices().scale(titleScale, titleScale, titleScale);
        font.drawWithShadow(event.getMatrices(), WinkelClient.NAME, 2, 2, -1);
        event.getMatrices().pop();

        event.getMatrices().push();
        event.getMatrices().translate(size + 4, 0, 0);
        event.getMatrices().scale(versionScale, versionScale, versionScale);
        font.drawWithShadow(event.getMatrices(), String.format("%s%s", Formatting.UNDERLINE, WinkelClient.VERSION),
                2 + (font.getWidth(WinkelClient.NAME) * titleScale) + 20, 2, -1);
        event.getMatrices().pop();

        // module-list
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

        tabGui.render(event.getMatrices(), event.getDelta());
    }

}

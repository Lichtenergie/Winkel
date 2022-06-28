package de.dietrichpaul.winkel.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WinkelScreen extends Screen {

    private static final Identifier BACKGROUND = new Identifier("winkel", "wallpaper.jpg");

    protected Text subTitle;

    public WinkelScreen(String name) {
        super(Text.translatable("screen." + name + ".title"));
        this.subTitle = Text.translatable("screen." + name + ".subtitle");
    }

    protected void renderContent(MatrixStack matrices, int mouseX, int mouseY, float delta) {

    }

    @Override
    public final void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        drawTexture(matrices, 0, 0, 0F, 0F, width, height, width, height);
        this.renderContent(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

}

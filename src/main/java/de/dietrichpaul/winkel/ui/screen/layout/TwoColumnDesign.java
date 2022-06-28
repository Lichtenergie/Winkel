package de.dietrichpaul.winkel.ui.screen.layout;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.ui.screen.WinkelScreen;
import de.dietrichpaul.winkel.ui.screen.widget.CenteredButtonList;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class TwoColumnDesign extends WinkelScreen {

    private static final int BLINKING_OFF = 4;
    private static final int BLINKING_ON = 4;
    private static final int CHAR_TYPE_DELAY = 2;
    private static final int CHAR_TYPE_START_DELAY = 22;

    private int blinking;
    private byte blinkingDirection;

    private int titleChars;
    private int typeDelay;
    private int typeStartDelay;

    public TwoColumnDesign(String name) {
        super(name);
    }

    @Override
    protected void init() {
        super.init();
        blinking = -BLINKING_OFF;
        blinkingDirection = 1;
        typeStartDelay = CHAR_TYPE_START_DELAY;
    }

    public CenteredButtonList addButtonListSidebar() {
        int navWidth = MathHelper.clamp(width - 317, 110, 200);
        int sideWidth = Math.min(navWidth - 10, 120);
        return addDrawableChild(new CenteredButtonList((navWidth - sideWidth) / 2, sideWidth, this.height / 2));
    }

    @Override
    public void tick() {
        blinking += blinkingDirection;
        if (blinking == -BLINKING_OFF) {
            blinkingDirection = 1;
        } else if (blinking == BLINKING_ON) {
            blinkingDirection = -1;
        }
        if (typeDelay > 0) {
            typeDelay--;
        }
        if (client.getOverlay() == null && typeStartDelay > 0) {
            typeStartDelay--;
        }
        if (typeStartDelay == 0 && typeDelay == 0 && titleChars < this.title.getString().length()) {
            System.out.println(this.title.getString());
            titleChars++;
            typeDelay = CHAR_TYPE_DELAY;
        }
        super.tick();
    }

    @Override
    public void renderContent(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int navWidth = MathHelper.clamp(width - 317, 110, 200);
        if (WinkelClient.DEBUG_GUI) {
            fill(matrices, 0, 0, navWidth, height, 0x13ff0000);
            fill(matrices, navWidth, 0, width, height, 0x1300ff00);
        }

        TextRenderer textRenderer = client.textRenderer;

        String textInTitle = this.title.getString();
        Text fullTitle = Text.literal(textInTitle);
        String title = "";
        if (this.titleChars > 0) {
            title += this.title.getString().substring(0, this.titleChars);
        }
        if (this.blinking >= 0 && this.titleChars < this.title.getString().length()) {
            title += "_";
        }

        // width = textWidth * scale | : textWidth
        // width:textWidth = scale
        float scale = MathHelper.floor(Math.min(2F, (navWidth - 25F) / textRenderer.getWidth(fullTitle)) / 0.5F) * 0.5F;
        matrices.push();
        matrices.scale(scale, scale, scale);
        textRenderer.draw(matrices, title, navWidth / (2.0F * scale) - textRenderer.getWidth(fullTitle) / 2.0F, navWidth / (2.0F * scale) - textRenderer.getWidth(fullTitle) / 2.0F, 0xffe0e0e0);
        matrices.pop();
    }

}

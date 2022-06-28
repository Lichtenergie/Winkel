package de.dietrichpaul.winkel.ui.screen.list;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.ui.screen.WinkelScreen;
import de.dietrichpaul.winkel.ui.screen.layout.TwoColumnDesign;
import de.dietrichpaul.winkel.ui.screen.widget.CenteredButtonList;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class WinkelMainMenu extends TwoColumnDesign {

    public WinkelMainMenu() {
        super("mainmenu");
    }

    @Override
    protected void init() {
        CenteredButtonList buttonList = addButtonListSidebar();
        buttonList.addButton(new CenteredButtonList.Entry(Text.translatable("menu.singleplayer"), null, () -> {
            client.setScreen(new SelectWorldScreen(this));
        }, null));
        buttonList.addButton(new CenteredButtonList.Entry(Text.translatable("menu.multiplayer"), null, () -> {
            client.setScreen(new MultiplayerScreen(this));
        }, null));
        buttonList.addButton(new CenteredButtonList.Entry(Text.translatable("menu.options"), null, () -> {
            client.setScreen(new OptionsScreen(this, client.options));
        }, null));
        buttonList.addButton(new CenteredButtonList.Entry(Text.translatable("menu.quit"), null, client::scheduleStop, null));
        super.init();
    }

    @Override
    public void renderContent(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderContent(matrices, mouseX, mouseY, delta);
        matrices.push();
        matrices.scale(0.625F, 0.625F, 0.625F);
        client.textRenderer.draw(matrices, "v" + WinkelClient.VERSION, 4, 1.6F * height - 22, 0xff8c8c8c);
        client.textRenderer.draw(matrices, "Made by Paul Dietrich", 4, 1.6F * height - 12, 0xff8c8c8c);
        matrices.pop();
    }
}

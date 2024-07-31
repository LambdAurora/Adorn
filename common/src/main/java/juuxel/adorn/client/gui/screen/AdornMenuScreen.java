package juuxel.adorn.client.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.MenuScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.menu.Menu;
import net.minecraft.text.Text;

public abstract class AdornMenuScreen<M extends Menu> extends MenuScreen<M> {
    public AdornMenuScreen(M menu, PlayerInventory playerInventory, Text title) {
        super(menu, playerInventory, title);
    }

    public int getPanelX() {
        return x;
    }

    public int getPanelY() {
        return y;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}

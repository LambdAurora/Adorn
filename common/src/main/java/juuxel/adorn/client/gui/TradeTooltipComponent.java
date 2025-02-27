package juuxel.adorn.client.gui;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.trading.Trade;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.util.Identifier;

public record TradeTooltipComponent(Trade trade) implements TooltipComponent {
    private static final Identifier ARROW_TEXTURE = AdornCommon.id("textures/gui/tooltip_arrow.png");

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 3 * 16;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        context.drawItem(trade.getPrice(), x, y);
        context.drawItemInSlot(textRenderer, trade.getPrice(), x, y);
        context.drawItem(trade.getSelling(), x + 2 * 16, y);
        context.drawItemInSlot(textRenderer, trade.getSelling(), x + 2 * 16, y);
        context.drawTexture(ARROW_TEXTURE, x + 16, y, 0f, 0f, 16, 16, 16, 16);
    }
}

package juuxel.adorn.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.client.FluidRenderingBridge;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidVolume;
import juuxel.adorn.menu.BrewerMenu;
import juuxel.adorn.util.Colors;
import juuxel.adorn.util.Logging;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.MenuProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;

import java.util.List;

public final class BrewerScreen extends AdornMenuScreen<BrewerMenu> {
    private static final Logger LOGGER = Logging.logger();
    public static final Identifier TEXTURE = AdornCommon.id("textures/gui/brewer.png");
    public static final int FLUID_AREA_HEIGHT = 59;

    public BrewerScreen(BrewerMenu menu, PlayerInventory playerInventory, Text title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        drawFluid(context, x + 145, y + 17, menu.getFluid());
        context.drawTexture(TEXTURE, x + 145, y + 21, 176, 25, 16, 51);

        var progress = menu.getProgress();
        if (progress > 0) {
            float progressFract = (float) progress / (float) BrewerBlockEntity.MAX_PROGRESS;
            context.drawTexture(TEXTURE, x + 84, y + 24, 176, 0, 8, MathHelper.ceil(progressFract * 25));
        }
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
        super.drawMouseoverTooltip(context, x, y);
        int x2 = x - this.x;
        int y2 = y - this.y;
        if (145 <= x2 && x2 < 145 + 16 && 17 <= y2 && y2 < 17 + FLUID_AREA_HEIGHT) {
            context.drawTooltip(textRenderer, getFluidTooltip(menu.getFluid()), x, y);
        }
    }


    private List<Text> getFluidTooltip(FluidReference fluid) {
        return FluidRenderingBridge.get().getTooltip(
            fluid,
            client.options.advancedItemTooltips ? TooltipContext.ADVANCED : TooltipContext.BASIC,
            BrewerBlockEntity.FLUID_CAPACITY_IN_BUCKETS * 1000
        );
    }

    public static void setFluidFromPacket(MinecraftClient client, int syncId, FluidVolume fluid) {
        if (client.currentScreen instanceof MenuProvider<?> menuProvider) {
            var menu = menuProvider.getMenu();
            if (menu.syncId == syncId && menu instanceof BrewerMenu brewerMenu) {
                brewerMenu.setFluid(fluid);
            }
        }
    }

    private static void drawSprite(DrawContext context, int x, float y, float width, float height, float u0, float v0, float u1, float v1, Sprite sprite, int color) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, sprite.getAtlasId());
        var buffer = Tessellator.getInstance().getBuffer();
        var positionMatrix = context.getMatrices().peek().getPositionMatrix();
        var au0 = MathHelper.lerp(u0, sprite.getMinU(), sprite.getMaxU());
        var au1 = MathHelper.lerp(u1, sprite.getMinU(), sprite.getMaxU());
        var av0 = MathHelper.lerp(v0, sprite.getMinV(), sprite.getMaxV());
        var av1 = MathHelper.lerp(v1, sprite.getMinV(), sprite.getMaxV());
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        buffer.vertex(positionMatrix, x, y + height, 0f).color(color).texture(au0, av1).next();
        buffer.vertex(positionMatrix, x + width, y + height, 0f).color(color).texture(au1, av1).next();
        buffer.vertex(positionMatrix, x + width, y, 0f).color(color).texture(au1, av0).next();
        buffer.vertex(positionMatrix, x, y, 0f).color(color).texture(au0, av0).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableBlend();
    }

    public static void drawFluid(DrawContext context, int x, int y, FluidReference fluid) {
        if (fluid.isEmpty()) return;

        var bridge = FluidRenderingBridge.get();
        var sprite = bridge.getStillSprite(fluid);
        if (sprite == null) {
            LOGGER.warn("Could not find sprite for {} in brewer screen", fluid);
            return;
        }

        var color = Colors.color(bridge.getColor(fluid));
        var height = FLUID_AREA_HEIGHT * (float) (fluid.getAmount() / (BrewerBlockEntity.FLUID_CAPACITY_IN_BUCKETS * fluid.getUnit().getBucketVolume()));
        var fluidY = 0;

        int tiles = MathHelper.floor(height / 16);
        for (int i = 0; i < tiles; i++) {
            drawSprite(context, x, y + transformY(bridge, fluid, fluidY, 16f), 16f, 16f, 0f, 0f, 1f, 1f, sprite, color);
            fluidY += 16;
        }

        var leftover = height % 16;
        drawSprite(context, x, y + transformY(bridge, fluid, fluidY, leftover), 16f, leftover, 0f, 0f, 1f, leftover / 16f, sprite, color);
    }

    private static float transformY(FluidRenderingBridge bridge, FluidReference fluid, int fluidY, float areaHeight) {
        if (bridge.fillsFromTop(fluid)) {
            return fluidY;
        } else {
            return FLUID_AREA_HEIGHT - fluidY - areaHeight;
        }
    }
}

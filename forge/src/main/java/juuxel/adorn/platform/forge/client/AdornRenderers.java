package juuxel.adorn.platform.forge.client;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.client.renderer.InvisibleEntityRenderer;
import juuxel.adorn.client.renderer.ShelfRenderer;
import juuxel.adorn.client.renderer.TradingStationRenderer;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.platform.forge.client.renderer.KitchenSinkRendererForge;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class AdornRenderers {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AdornEntities.SEAT.get(), InvisibleEntityRenderer::new);
        event.registerBlockEntityRenderer(AdornBlockEntities.TRADING_STATION.get(), TradingStationRenderer::new);
        event.registerBlockEntityRenderer(AdornBlockEntities.SHELF.get(), ShelfRenderer::new);
        event.registerBlockEntityRenderer(forceType(AdornBlockEntities.KITCHEN_SINK.get()), KitchenSinkRendererForge::new);
    }

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity, U extends T> BlockEntityType<U> forceType(BlockEntityType<T> type) {
        return (BlockEntityType<U>) type;
    }
}

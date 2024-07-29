package juuxel.adorn.platform.forge.client

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.client.renderer.InvisibleEntityRenderer
import juuxel.adorn.client.renderer.ShelfRenderer
import juuxel.adorn.client.renderer.TradingStationRenderer
import juuxel.adorn.entity.AdornEntities
import juuxel.adorn.platform.forge.block.entity.KitchenSinkBlockEntityForge
import juuxel.adorn.platform.forge.client.renderer.KitchenSinkRendererForge
import net.minecraft.block.entity.BlockEntityType
import net.neoforged.neoforge.client.event.EntityRenderersEvent

object AdornRenderers {
    fun registerRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(AdornEntities.SEAT.get(), ::InvisibleEntityRenderer)
        event.registerBlockEntityRenderer(AdornBlockEntities.TRADING_STATION.get(), ::TradingStationRenderer)
        event.registerBlockEntityRenderer(AdornBlockEntities.SHELF.get(), ::ShelfRenderer)
        @Suppress("UNCHECKED_CAST")
        event.registerBlockEntityRenderer(AdornBlockEntities.KITCHEN_SINK.get() as BlockEntityType<KitchenSinkBlockEntityForge>, ::KitchenSinkRendererForge)
    }
}

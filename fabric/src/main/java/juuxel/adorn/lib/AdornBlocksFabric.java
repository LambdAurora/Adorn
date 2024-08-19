package juuxel.adorn.lib;

import juuxel.adorn.CommonEventHandlers;
import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.SneakClickHandler;
import juuxel.adorn.block.entity.BrewerBlockEntityFabric;
import juuxel.adorn.block.entity.KitchenSinkBlockEntityFabric;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.client.renderer.KitchenSinkRendererFabric;
import juuxel.adorn.client.renderer.ShelfRenderer;
import juuxel.adorn.client.renderer.TradingStationRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.ActionResult;

public final class AdornBlocksFabric {
    public static void init() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            var state = world.getBlockState(hitResult.getBlockPos());
            // Check that:
            // - the block is a sneak-click handler
            // - the player is sneaking
            // - the player isn't holding an item (for block item and bucket support)
            if (state.getBlock() instanceof SneakClickHandler handler && player.isSneaking() && player.getStackInHand(hand).isEmpty()) {
                return handler.onSneakClick(state, world, hitResult.getBlockPos(), player, hand, hitResult);
            } else {
                return ActionResult.PASS;
            }
        });

        UseBlockCallback.EVENT.register(CommonEventHandlers::handleCarpets);
        FluidStorage.SIDED.registerForBlockEntity(
            (brewer, side) -> ((BrewerBlockEntityFabric) brewer).getFluidStorage(),
            AdornBlockEntities.BREWER.get()
        );

        OxidizableBlocksRegistry.registerOxidizableBlockPair(AdornBlocks.COPPER_PIPE.get(), AdornBlocks.EXPOSED_COPPER_PIPE.get());
        OxidizableBlocksRegistry.registerOxidizableBlockPair(AdornBlocks.EXPOSED_COPPER_PIPE.get(), AdornBlocks.WEATHERED_COPPER_PIPE.get());
        OxidizableBlocksRegistry.registerOxidizableBlockPair(AdornBlocks.WEATHERED_COPPER_PIPE.get(), AdornBlocks.OXIDIZED_COPPER_PIPE.get());
        OxidizableBlocksRegistry.registerWaxableBlockPair(AdornBlocks.COPPER_PIPE.get(), AdornBlocks.WAXED_COPPER_PIPE.get());
        OxidizableBlocksRegistry.registerWaxableBlockPair(AdornBlocks.EXPOSED_COPPER_PIPE.get(), AdornBlocks.WAXED_EXPOSED_COPPER_PIPE.get());
        OxidizableBlocksRegistry.registerWaxableBlockPair(AdornBlocks.WEATHERED_COPPER_PIPE.get(), AdornBlocks.WAXED_WEATHERED_COPPER_PIPE.get());
        OxidizableBlocksRegistry.registerWaxableBlockPair(AdornBlocks.OXIDIZED_COPPER_PIPE.get(), AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE.get());

        FlammableBlockRegistry.getDefaultInstance().add(AdornTags.PAINTED_PLANKS.block(), 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(AdornTags.PAINTED_WOOD_SLABS.block(), 5, 20);
    }

    public static void afterRegister() {
        for (var kitchenSink : BlockVariantSets.get(BlockKind.KITCHEN_SINK)) {
            FluidStorage.SIDED.registerForBlocks(KitchenSinkBlockEntityFabric.FLUID_STORAGE_PROVIDER, kitchenSink.get());
        }
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        // BlockEntityRenderers
        BlockEntityRendererFactories.register(AdornBlockEntities.TRADING_STATION.get(), TradingStationRenderer::new);
        BlockEntityRendererFactories.register(AdornBlockEntities.SHELF.get(), ShelfRenderer::new);
        BlockEntityRendererFactories.register(forceType(AdornBlockEntities.KITCHEN_SINK.get()), KitchenSinkRendererFabric::new);

        // RenderLayers
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutout(),
            AdornBlocks.TRADING_STATION.get(),
            AdornBlocks.STONE_TORCH_GROUND.get(),
            AdornBlocks.STONE_TORCH_WALL.get(),
            AdornBlocks.CHAIN_LINK_FENCE.get(),
            AdornBlocks.STONE_LADDER.get(),
            AdornBlocks.CANDLELIT_LANTERN.get()
        );

        for (var block : AdornBlocks.DYED_CANDLELIT_LANTERNS.get().values()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }

        for (var coffeeTable : BlockVariantSets.get(BlockKind.COFFEE_TABLE)) {
            BlockRenderLayerMap.INSTANCE.putBlock(coffeeTable.get(), RenderLayer.getTranslucent());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity, U extends T> BlockEntityType<U> forceType(BlockEntityType<T> type) {
        return (BlockEntityType<U>) type;
    }
}

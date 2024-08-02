package juuxel.adorn.platform.forge.event;

import juuxel.adorn.CommonEventHandlers;
import juuxel.adorn.block.SneakClickHandler;
import net.minecraft.util.ActionResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class BlockEvents {
    public static void init() {
        NeoForge.EVENT_BUS.addListener(BlockEvents::handleCarpetedBlocks);
        NeoForge.EVENT_BUS.addListener(BlockEvents::handleSneakClicks);
    }

    private static void handleCarpetedBlocks(PlayerInteractEvent.RightClickBlock event) {
        ActionResult result = CommonEventHandlers.handleCarpets(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());

        if (result != ActionResult.PASS) {
            event.setCancellationResult(result);
            event.setCanceled(true);
        }
    }

    private static void handleSneakClicks(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getEntity();
        var state = event.getLevel().getBlockState(event.getPos());

        // Check that:
        // - the block is a sneak-click handler
        // - the player is sneaking
        // - the player isn't holding an item (for block item and bucket support)
        if (state.getBlock() instanceof SneakClickHandler clickHandler && player.isSneaking() && player.getStackInHand(event.getHand()).isEmpty()) {
            var result = clickHandler.onSneakClick(state, event.getLevel(), event.getPos(), player, event.getHand(), event.getHitVec());

            if (result != ActionResult.PASS) {
                event.setCancellationResult(result);
                event.setCanceled(true);
            }
        }
    }
}

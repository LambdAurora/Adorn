package juuxel.adorn.item;

import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;

public final class CarpetedTopPlacementContext extends ItemPlacementContext {
    public CarpetedTopPlacementContext(ItemUsageContext context) {
        super(context);
        // We know that the block is a carpet block
        canReplaceExisting = true;
    }
}

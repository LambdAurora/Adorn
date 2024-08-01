package juuxel.adorn.item;

import net.minecraft.block.Block;
import net.minecraft.block.CarpetBlock;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;

public final class ChairBlockItem extends AdornTallBlockItem {
    public ChairBlockItem(Block block) {
        super(block, new Settings());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos();
        if (context.getSide() == Direction.UP && world.getBlockState(pos).getBlock() instanceof CarpetBlock) {
            return place(new CarpetedTopPlacementContext(context));
        }

        return super.useOnBlock(context);
    }
}

package juuxel.adorn.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implement on blocks to give them a sneak-click action.
 */
public interface SneakClickHandler {
    ActionResult onSneakClick(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult);
}

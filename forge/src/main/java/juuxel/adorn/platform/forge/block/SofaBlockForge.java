package juuxel.adorn.platform.forge.block;

import juuxel.adorn.block.SofaBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public final class SofaBlockForge extends SofaBlock {
    public SofaBlockForge(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isBed(BlockState state, BlockView level, BlockPos pos, LivingEntity sleeper) {
        return true;
    }

    @Override
    public Direction getBedDirection(BlockState state, WorldView world, BlockPos pos) {
        var direction = getSleepingDirection(world, pos);
        return direction != null ? direction.getOpposite() : null;
    }

    @Override
    public void setBedOccupied(BlockState state, World world, BlockPos pos, LivingEntity sleeper, boolean occupied) {
        super.setBedOccupied(state, world, pos, sleeper, occupied);
        var neighborPos = pos.offset(getSleepingDirection(world, pos, true));
        world.setBlockState(neighborPos, world.getBlockState(neighborPos).with(OCCUPIED, occupied));
    }
}

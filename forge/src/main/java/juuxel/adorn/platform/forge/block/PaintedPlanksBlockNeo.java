package juuxel.adorn.platform.forge.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public final class PaintedPlanksBlockNeo extends Block {
    public static final int BURN_CHANCE = 5;
    public static final int SPREAD_CHANCE = 20;

    public PaintedPlanksBlockNeo(Settings settings) {
        super(settings);
    }

    // getFireSpreadSpeed = burnChance
    // getFlammability = spreadChance

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockView level, BlockPos pos, Direction direction) {
        return BURN_CHANCE;
    }

    @Override
    public int getFlammability(BlockState state, BlockView level, BlockPos pos, Direction direction) {
        return SPREAD_CHANCE;
    }
}

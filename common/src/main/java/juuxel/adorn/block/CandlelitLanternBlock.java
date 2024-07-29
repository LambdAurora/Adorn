package juuxel.adorn.block;

import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public final class CandlelitLanternBlock extends LanternBlock implements BlockWithDescription {
    private static final String DESCRIPTION_KEY = "block.adorn.candlelit_lantern.description";

    public CandlelitLanternBlock(Settings settings) {
        super(settings);
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        var px = 1 / 16.0;
        var vec = Vec3d.ofCenter(pos, state.get(HANGING) ? 6 * px : 5 * px);
        AbstractCandleBlock.spawnCandleParticles(world, vec, random);
    }

    public static Settings createBlockSettings() {
        return Settings.create()
            .mapColor(MapColor.IRON_GRAY)
            .solid()
            .requiresTool()
            .strength(3.5f)
            .sounds(BlockSoundGroup.LANTERN)
            .luminance(state -> 12)
            .nonOpaque();
    }
}

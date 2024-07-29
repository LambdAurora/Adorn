package juuxel.adorn.block;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;

public final class FluidUtil {
    /**
     * For using a mixin (juuxel.adorn.mixin.fluidloggable.FluidUtilMixin) to set the fluid property
     * for Towelette support.
     */
    public static BlockState updateFluidFromState(BlockState state, FluidState fluidState) {
        return state.with(Properties.WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }
}

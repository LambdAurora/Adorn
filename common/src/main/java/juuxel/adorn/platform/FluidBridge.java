package juuxel.adorn.platform;

import juuxel.adorn.fluid.FluidAmountPredicate;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.fluid.FluidVolume;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@InlineServices
public interface FluidBridge {
    FluidUnit getFluidUnit();

    @Nullable FluidVolume drain(World world, BlockPos pos, @Nullable BlockState state, Direction side, Fluid fluid, FluidAmountPredicate amountPredicate);

    @InlineServices.Getter
    static FluidBridge get() {
        return Services.load(FluidBridge.class);
    }
}

package juuxel.adorn.platform.forge;

import juuxel.adorn.fluid.FluidAmountPredicate;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.fluid.FluidVolume;
import juuxel.adorn.platform.FluidBridge;
import juuxel.adorn.platform.forge.util.FluidTankReference;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public final class FluidBridgeForge implements FluidBridge {
    @Override
    public FluidUnit getFluidUnit() {
        return FluidUnit.LITRE;
    }

    @Nullable
    @Override
    public FluidVolume drain(World world, BlockPos pos, @Nullable BlockState state, Direction side, Fluid fluid, FluidAmountPredicate amountPredicate) {
        // This method is a port of the Fabric fluid bridge code.
        var fluidHandler = world.getCapability(Capabilities.FluidHandler.BLOCK, pos, state, null, side);

        if (fluidHandler != null) {
            var upperBound = amountPredicate.getUpperBound();
            int maxAmount = (int) FluidUnit.convert(upperBound.getAmount(), upperBound.getUnit(), FluidUnit.LITRE);
            var max = new FluidStack(fluid, maxAmount);
            var extracted = fluidHandler.drain(max, IFluidHandler.FluidAction.SIMULATE);

            if (!extracted.isEmpty() && amountPredicate.test(extracted.getAmount(), FluidUnit.LITRE)) {
                fluidHandler.drain(extracted, IFluidHandler.FluidAction.EXECUTE);
                return FluidTankReference.toFluidVolume(extracted);
            }
        }

        return null;
    }
}

package juuxel.adorn.platform.fabric;

import juuxel.adorn.fluid.FluidAmountPredicate;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.fluid.FluidVolume;
import juuxel.adorn.platform.FluidBridge;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class FluidBridgeFabric implements FluidBridge {
    @Override
    public FluidUnit getFluidUnit() {
        return FluidUnit.DROPLET;
    }

    @Override
    public @Nullable FluidVolume drain(World world, BlockPos pos, @Nullable BlockState state, Direction side, Fluid fluid, FluidAmountPredicate amountPredicate) {
        var storage = FluidStorage.SIDED.find(world, pos, state, null, side);

        if (storage != null) {
            var upperBound = amountPredicate.getUpperBound();
            var maxAmount = FluidUnit.convert(upperBound.getAmount(), upperBound.getUnit(), FluidUnit.DROPLET);
            try (var transaction = Transaction.openOuter()) {
                var extracted = storage.extract(FluidVariant.of(fluid), maxAmount, transaction);

                if (extracted > 0 && amountPredicate.test(extracted, FluidUnit.DROPLET)) {
                    transaction.commit();
                    return new FluidVolume(fluid, extracted, null, FluidUnit.DROPLET);
                }
            }
        }

        return null;
    }
}

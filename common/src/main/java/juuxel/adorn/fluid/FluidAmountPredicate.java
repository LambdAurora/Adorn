package juuxel.adorn.fluid;

import net.minecraft.component.ComponentChanges;
import net.minecraft.fluid.Fluids;

public interface FluidAmountPredicate {
    HasFluidAmount getUpperBound();

    boolean test(long amount, FluidUnit unit);

    static FluidAmountPredicate exactly(long amount, FluidUnit unit) {
        return new FluidAmountPredicate() {
            private final FluidVolume upperBound = new FluidVolume(Fluids.EMPTY, amount, ComponentChanges.EMPTY, unit);

            @Override
            public HasFluidAmount getUpperBound() {
                return upperBound;
            }

            @Override
            public boolean test(long amount, FluidUnit unit) {
                return FluidUnit.compareVolumes(amount, unit, upperBound.getAmount(), upperBound.getUnit()) == 0;
            }
        };
    }

    static FluidAmountPredicate atMost(long max, FluidUnit unit) {
        return new FluidAmountPredicate() {
            private final FluidVolume upperBound = new FluidVolume(Fluids.EMPTY, max, ComponentChanges.EMPTY, unit);

            @Override
            public HasFluidAmount getUpperBound() {
                return upperBound;
            }

            @Override
            public boolean test(long amount, FluidUnit unit) {
                return FluidUnit.compareVolumes(amount, unit, upperBound.getAmount(), upperBound.getUnit()) <= 0;
            }
        };
    }
}

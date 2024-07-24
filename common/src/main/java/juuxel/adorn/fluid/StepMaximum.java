package juuxel.adorn.fluid;

public final class StepMaximum implements FluidAmountPredicate {
    private final long min;
    private final long max;
    private final long step;
    private final FluidUnit unit;
    private final HasFluidAmount upperBound;

    public StepMaximum(long min, long max, long step, FluidUnit unit) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max");
        } else if ((max - min) % step != 0L) {
            throw new IllegalArgumentException("max - min must be divisible by step");
        }

        this.min = min;
        this.max = max;
        this.step = step;
        this.unit = unit;
        this.upperBound = new HasFluidAmount() {
            @Override
            public long getAmount() {
                return StepMaximum.this.max;
            }

            @Override
            public FluidUnit getUnit() {
                return StepMaximum.this.unit;
            }
        };
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public long getStep() {
        return step;
    }

    public FluidUnit getUnit() {
        return unit;
    }

    @Override
    public HasFluidAmount getUpperBound() {
        return upperBound;
    }

    @Override
    public boolean test(long amount, FluidUnit unit) {
        long toCompare = FluidUnit.convert(amount, unit, this.unit);

        if (toCompare < min || toCompare > max) {
            return false;
        }

        long zeroed = toCompare - min;
        return zeroed % step == 0L;
    }
}

package juuxel.adorn.fluid;

/**
 * A fluid volume-like object that has a fluid amount and a unit.
 */
public interface HasFluidAmount {
    long getAmount();
    FluidUnit getUnit();
}

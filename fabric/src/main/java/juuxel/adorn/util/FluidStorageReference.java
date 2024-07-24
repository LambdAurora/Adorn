package juuxel.adorn.util;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidUnit;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

/**
 * A {@linkplain FluidReference fluid reference} to a {@link SingleVariantStorage}.
 */
public final class FluidStorageReference extends FluidReference {
    private final SingleVariantStorage<FluidVariant> storage;

    public FluidStorageReference(SingleVariantStorage<FluidVariant> storage) {
        this.storage = storage;
    }

    public FluidVariant getVariant() {
        return storage.variant;
    }

    @Override
    public Fluid getFluid() {
        return storage.variant.getFluid();
    }

    @Override
    public void setFluid(Fluid fluid) {
        storage.variant = FluidVariant.of(fluid, storage.variant.getNbt());
    }

    @Override
    public long getAmount() {
        return storage.amount;
    }

    @Override
    public void setAmount(long amount) {
        storage.amount = amount;
    }

    @Override
    public @Nullable NbtCompound getNbt() {
        return storage.variant.getNbt();
    }

    @Override
    public void setNbt(@Nullable NbtCompound nbt) {
        storage.variant = FluidVariant.of(storage.variant.getFluid(), nbt);
    }

    @Override
    public FluidUnit getUnit() {
        return FluidUnit.DROPLET;
    }

    /**
     * Converts this fluid reference to a {@link FluidVariant}.
     * This is faster than a manual conversion for a {@code FluidStorageReference}.
     */
    public static FluidVariant toFluidVariant(FluidReference reference) {
        if (reference instanceof FluidStorageReference fsr) {
            return fsr.getVariant();
        } else {
            return FluidVariant.of(reference.getFluid(), reference.getNbt());
        }
    }
}

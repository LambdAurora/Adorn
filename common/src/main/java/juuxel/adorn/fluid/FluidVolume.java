package juuxel.adorn.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

public final class FluidVolume extends FluidReference {
    private Fluid fluid;
    private long amount;
    private @Nullable NbtCompound nbt;
    private final FluidUnit unit;

    public FluidVolume(Fluid fluid, long amount, @Nullable NbtCompound nbt, FluidUnit unit) {
        this.fluid = fluid;
        this.amount = amount;
        this.nbt = nbt;
        this.unit = unit;
    }

    public static FluidVolume empty(FluidUnit unit) {
        return new FluidVolume(Fluids.EMPTY, 0, null, unit);
    }

    public static FluidVolume load(PacketByteBuf buf) {
        var volume = empty(buf.readEnumConstant(FluidUnit.class));
        volume.readWithoutUnit(buf);
        return volume;
    }

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public void setFluid(Fluid fluid) {
        this.fluid = fluid;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public @Nullable NbtCompound getNbt() {
        return nbt;
    }

    @Override
    public void setNbt(@Nullable NbtCompound nbt) {
        this.nbt = nbt;
    }

    @Override
    public FluidUnit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "FluidVolume(fluid=%s, amount=%d, nbt=%s)".formatted(fluid, amount, nbt);
    }
}

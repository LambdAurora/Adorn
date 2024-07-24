package juuxel.adorn.fluid;

import juuxel.adorn.config.ConfigManager;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A mutable reference to a fluid volume.
 * This can be a {@link FluidVolume} or a block entity's
 * internal fluid volume.
 */
public abstract class FluidReference implements HasFluidAmount {
    public abstract Fluid getFluid();
    public abstract void setFluid(Fluid fluid);

    public abstract void setAmount(long amount);

    public abstract @Nullable NbtCompound getNbt();
    public abstract void setNbt(@Nullable NbtCompound nbt);

    public boolean isEmpty() {
        return getFluid() == Fluids.EMPTY || getAmount() == 0;
    }

    public void write(PacketByteBuf buf) {
        buf.writeEnumConstant(getUnit());

        if (isEmpty()) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeVarInt(Registries.FLUID.getRawId(getFluid()));
            buf.writeVarLong(getAmount());
            buf.writeNbt(getNbt());
        }
    }

    protected void readWithoutUnit(PacketByteBuf buf) {
        if (buf.readBoolean()) {
            setFluid(Registries.FLUID.get(buf.readVarInt()));
            setAmount(buf.readVarLong());
            setNbt(buf.readNbt());
        } else {
            setFluid(Fluids.EMPTY);
            setAmount(0);
            setNbt(null);
        }
    }

    /**
     * Creates an independent mutable snapshot of this fluid reference's current contents.
     */
    public FluidVolume createSnapshot() {
        return new FluidVolume(getFluid(), getAmount(), getNbt(), getUnit());
    }

    public void increment(long amount, FluidUnit unit) {
        setAmount(getAmount() + FluidUnit.convert(amount, unit, getUnit()));
    }

    public void decrement(long amount, FluidUnit unit) {
        increment(-amount, unit);
    }

    public boolean matches(FluidIngredient ingredient) {
        return ingredient.fluid().matches(getFluid())
            && FluidUnit.compareVolumes(this, ingredient) >= 0
            && Objects.equals(getNbt(), ingredient.nbt());
    }

    public Text getAmountText() {
        var displayUnit = getDefaultDisplayUnit();
        return Text.translatable(
            "gui.adorn.fluid_volume",
            FluidUnit.losslessConvert(getAmount(), getUnit(), displayUnit).resizeFraction(getUnitDenominator(getUnit(), displayUnit)),
            displayUnit.getSymbol()
        );
    }

    public Text getAmountText(long max, FluidUnit maxUnit) {
        var displayUnit = getDefaultDisplayUnit();
        return Text.translatable(
            "gui.adorn.fluid_volume.fraction",
            FluidUnit.losslessConvert(getAmount(), getUnit(), displayUnit).resizeFraction(getUnitDenominator(getUnit(), displayUnit)),
            FluidUnit.losslessConvert(max, maxUnit, displayUnit).resizeFraction(getUnitDenominator(maxUnit, displayUnit)),
            displayUnit.getSymbol()
        );
    }

    private static FluidUnit getDefaultDisplayUnit() {
        return ConfigManager.Companion.config().client.displayedFluidUnit;
    }

    private static long getUnitDenominator(FluidUnit from, FluidUnit to) {
        if (from.getBucketVolume() == to.getBucketVolume()) return 1;
        return Math.max(1, from.getBucketVolume() / to.getBucketVolume());
    }

    @Override
    public String toString() {
        return "FluidReference(fluid=%s, amount=%d, nbt=%s)"
            .formatted(Registries.FLUID.getId(getFluid()), getAmount(), getNbt());
    }

    public static boolean areFluidsEqual(FluidReference a, FluidReference b) {
        if (a.isEmpty()) return b.isEmpty();
        return a.getFluid() == b.getFluid() && Objects.equals(a.getNbt(), b.getNbt());
    }

    public static boolean areFluidsAndAmountsEqual(FluidReference a, FluidReference b) {
        if (a.isEmpty()) return b.isEmpty();
        return areFluidsEqual(a, b) && FluidUnit.compareVolumes(a, b) == 0;
    }
}

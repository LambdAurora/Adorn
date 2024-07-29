package juuxel.adorn.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A fluid ingredient for crafting.
 */
public record FluidIngredient(FluidKey fluid, long amount, @Nullable NbtCompound nbt, FluidUnit unit) implements HasFluidAmount {
    public static final Codec<FluidIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        FluidKey.CODEC.fieldOf("fluid").forGetter(FluidIngredient::fluid),
        Codec.LONG.fieldOf("amount").forGetter(FluidIngredient::amount),
        NbtCompound.CODEC.optionalFieldOf("nbt").forGetter(ingredient -> Optional.ofNullable(ingredient.nbt)),
        FluidUnit.CODEC.optionalFieldOf("unit", FluidUnit.LITRE).forGetter(FluidIngredient::unit)
    ).apply(instance, FluidIngredient::new));

    // for DFU
    private FluidIngredient(FluidKey fluid, long amount, Optional<NbtCompound> nbt, FluidUnit unit) {
        this(fluid, amount, nbt.orElse(null), unit);
    }

    public static FluidIngredient load(PacketByteBuf buf) {
        var fluid = FluidKey.load(buf);
        var amount = buf.readVarLong();
        var nbt = buf.readNbt();
        var unit = buf.readEnumConstant(FluidUnit.class);
        return new FluidIngredient(fluid, amount, nbt, unit);
    }

    public void write(PacketByteBuf buf) {
        fluid.write(buf);
        buf.writeVarLong(amount);
        buf.writeNbt(nbt);
        buf.writeEnumConstant(unit);
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public FluidUnit getUnit() {
        return unit;
    }
}

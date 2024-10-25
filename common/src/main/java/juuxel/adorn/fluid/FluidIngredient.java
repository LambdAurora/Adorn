package juuxel.adorn.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentChanges;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

/**
 * A fluid ingredient for crafting.
 */
public record FluidIngredient(FluidKey fluid, long amount, ComponentChanges components, FluidUnit unit) implements HasFluidAmount {
    public static final Codec<FluidIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        FluidKey.CODEC.fieldOf("fluid").forGetter(FluidIngredient::fluid),
        Codec.LONG.fieldOf("amount").forGetter(FluidIngredient::amount),
        ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(FluidIngredient::components),
        FluidUnit.CODEC.optionalFieldOf("unit", FluidUnit.LITRE).forGetter(FluidIngredient::unit)
    ).apply(instance, FluidIngredient::new));
    public static final PacketCodec<RegistryByteBuf, FluidIngredient> PACKET_CODEC =
        PacketCodec.of(FluidIngredient::write, FluidIngredient::load);

    public FluidIngredient(FluidKey fluid, long amount, FluidUnit unit) {
        this(fluid, amount, ComponentChanges.EMPTY, unit);
    }

    private static FluidIngredient load(RegistryByteBuf buf) {
        var fluid = FluidKey.load(buf);
        var amount = buf.readVarLong();
        var components = ComponentChanges.PACKET_CODEC.decode(buf);
        var unit = buf.readEnumConstant(FluidUnit.class);
        return new FluidIngredient(fluid, amount, components, unit);
    }

    private void write(RegistryByteBuf buf) {
        fluid.write(buf);
        buf.writeVarLong(amount);
        ComponentChanges.PACKET_CODEC.encode(buf, components);
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

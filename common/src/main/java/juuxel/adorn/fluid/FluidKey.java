package juuxel.adorn.fluid;

import com.mojang.serialization.Codec;
import juuxel.adorn.util.EntryOrTag;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/// A "key" that identifies a fluid or a group of fluids.
///
/// Could be a single fluid, a tag or a list of the former.
///
/// ## JSON format
///
/// A fluid key is one of:
///
/// - a string; if prefixed with `#`, a tag, otherwise a fluid ID
/// - an array of strings as described above
///
/// Examples: `"minecraft:water"`, `"#c:milk"`, `["minecraft:water", "minecraft:lava"]`
public sealed interface FluidKey permits FluidKeyImpl.Simple, FluidKeyImpl.OfArray {
    Codec<FluidKey> CODEC = FluidKeyImpl.CODEC;

    /**
     * Returns the set of all fluids matching this key.
     */
    Set<Fluid> getFluids();

    /**
     * Tests whether the fluid matches this key.
     */
    boolean matches(Fluid fluid);

    /**
     * Writes this key to a packet buffer.
     * @see #load
     */
    default void write(PacketByteBuf buf) {
        var fluids = getFluids();
        buf.writeVarInt(fluids.size());

        for (Fluid fluid : fluids) {
            buf.writeVarInt(Registries.FLUID.getRawId(fluid));
        }
    }

    /**
     * Reads a key from a packet buffer.
     * @see #write
     */
    static FluidKey load(PacketByteBuf buf) {
        var size = buf.readVarInt();

        if (size == 1) {
            return new FluidKeyImpl.Simple(new EntryOrTag.OfEntry<>(Registries.FLUID.get(buf.readVarInt())));
        } else {
            List<FluidKeyImpl.Simple> children = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                children.add(new FluidKeyImpl.Simple(new EntryOrTag.OfEntry<>(Registries.FLUID.get(buf.readVarInt()))));
            }
            return new FluidKeyImpl.OfArray(children);
        }
    }
}

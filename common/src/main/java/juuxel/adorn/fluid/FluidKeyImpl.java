package juuxel.adorn.fluid;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

final class FluidKeyImpl {
    private static final Codec<Simple> SIMPLE_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<FluidKeyImpl.Simple, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input)
                .map(s -> {
                    if (s.startsWith("#")) {
                        return new OfTag(TagKey.of(RegistryKeys.FLUID, new Identifier(s.substring(1))));
                    } else {
                        return new OfFluid(Registries.FLUID.get(new Identifier(s)));
                    }
                })
                .map(key -> Pair.of(key, ops.empty()));
        }

        @Override
        public <T> DataResult<T> encode(FluidKeyImpl.Simple input, DynamicOps<T> ops, T prefix) {
            return ops.mergeToPrimitive(prefix, ops.createString(input.getId()));
        }

        @Override
        public String toString() {
            return "SimpleFluidKey";
        }
    };

    // TODO: Switch to either instead of xor
    public static final Codec<FluidKey> CODEC = Codecs.xor(
        SIMPLE_CODEC,
        SIMPLE_CODEC.listOf().xmap(OfArray::new, OfArray::children)
    ).xmap(
        // TODO (Java 21): Use pattern matching
        either -> either.map(Function.identity(), Function.identity()),
        key -> switch (key) {
            case Simple simple -> Either.left(simple);
            case OfArray ofArray -> Either.right(ofArray);
        }
    );

    sealed interface Simple extends FluidKey {
        String getId();
    }

    record OfFluid(Fluid fluid) implements Simple {
        @Override
        public String getId() {
            return Registries.FLUID.getId(fluid).toString();
        }

        @Override
        public Set<Fluid> getFluids() {
            return Set.of(fluid);
        }

        @Override
        public boolean matches(Fluid fluid) {
            return fluid == this.fluid;
        }
    }

    record OfTag(TagKey<Fluid> tag) implements Simple {
        @Override
        public String getId() {
            return "#" + tag.id();
        }

        @Override
        public Set<Fluid> getFluids() {
            return Registries.FLUID.getOrCreateEntryList(tag)
                .stream()
                .map(RegistryEntry::value)
                .collect(Collectors.toSet());
        }

        @Override
        public boolean matches(Fluid fluid) {
            return fluid.isIn(tag);
        }
    }

    record OfArray(List<Simple> children) implements FluidKey {
        @Override
        public Set<Fluid> getFluids() {
            return children.stream()
                .flatMap(child -> child.getFluids().stream())
                .collect(Collectors.toSet());
        }

        @Override
        public boolean matches(Fluid fluid) {
            for (var child : children) {
                if (child.matches(fluid)) return true;
            }

            return false;
        }
    }
}

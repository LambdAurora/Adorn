package juuxel.adorn.fluid;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import juuxel.adorn.util.EntryOrTag;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

final class FluidKeyImpl {
    private static final Codec<Simple> SIMPLE_CODEC = EntryOrTag.codec(RegistryKeys.FLUID).xmap(Simple::new, Simple::fluids);

    public static final Codec<FluidKey> CODEC = Codec.either(
        SIMPLE_CODEC,
        SIMPLE_CODEC.listOf().xmap(OfArray::new, OfArray::children)
    ).xmap(
        Either::unwrap,
        key -> switch (key) {
            case Simple simple -> Either.left(simple);
            case OfArray ofArray -> Either.right(ofArray);
        }
    );

    record Simple(EntryOrTag<Fluid> fluids) implements FluidKey {
        @Override
        public Set<Fluid> getFluids() {
            return switch (fluids) {
                case EntryOrTag.OfEntry(var fluid) -> Set.of(fluid);
                case EntryOrTag.OfTag(var tag) -> Registries.FLUID.getOptional(tag)
                    .stream()
                    .flatMap(RegistryEntryList::stream)
                    .map(RegistryEntry::value)
                    .collect(Collectors.toSet());
            };
        }

        @Override
        public boolean matches(Fluid fluid) {
            return switch (fluids) {
                case EntryOrTag.OfEntry(var keyFluid) -> fluid.matchesType(keyFluid);
                case EntryOrTag.OfTag(var tag) -> fluid.isIn(tag);
            };
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

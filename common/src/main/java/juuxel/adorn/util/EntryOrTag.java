package juuxel.adorn.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;

public sealed interface EntryOrTag<T> {
    @SuppressWarnings("unchecked")
    static <T> Codec<EntryOrTag<T>> codec(RegistryKey<Registry<T>> registryKey) {
        return Codec.either(
            TagKey.codec(registryKey),
            (Codec<T>) Registries.REGISTRIES.get(registryKey.getValue()).getCodec()
        ).xmap(either -> either.map(EntryOrTag.OfTag::new, EntryOrTag.OfEntry::new), entryOrTag -> switch (entryOrTag) {
            case EntryOrTag.OfEntry(T value) -> Either.right(value);
            case EntryOrTag.OfTag(TagKey<T> inner) -> Either.left(inner);
        });
    }

    record OfEntry<T>(T value) implements EntryOrTag<T> {
    }

    record OfTag<T>(TagKey<T> tag) implements EntryOrTag<T> {
    }
}

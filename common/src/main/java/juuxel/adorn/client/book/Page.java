package juuxel.adorn.client.book;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.util.EntryOrTag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Page(List<Icon> icons, Text title, Text text, @Nullable Image image) {
    public static final Codec<Page> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Icon.CODEC.listOf().fieldOf("icons").forGetter(Page::icons),
        TextCodecs.CODEC.fieldOf("title").forGetter(Page::title),
        TextCodecs.CODEC.optionalFieldOf("text", Text.empty()).forGetter(Page::text),
        Image.CODEC.optionalFieldOf("image").forGetter(page -> Optional.ofNullable(page.image))
    ).apply(instance, Page::new));

    // For DFU
    private Page(List<Icon> icons, Text title, Text text, Optional<Image> image) {
        this(icons, title, text, image.orElse(null));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final List<Icon> icons = new ArrayList<>();
        private @Nullable Text title;
        private @Nullable Text text;
        private @Nullable Image image;

        private Builder() {
        }

        public Builder icon(ItemConvertible item) {
            icons.add(new Icon(new EntryOrTag.OfEntry<>(item.asItem())));
            return this;
        }

        public Builder icon(TagKey<Item> tag) {
            icons.add(new Icon(new EntryOrTag.OfTag<>(tag)));
            return this;
        }

        public Builder title(Text title) {
            this.title = title;
            return this;
        }

        public Builder content(Text text) {
            this.text = text;
            return this;
        }

        public Builder image(Image image) {
            this.image = image;
            return this;
        }

        public Page build() {
            if (icons.isEmpty()) throw new IllegalArgumentException("Page has no icons");
            Objects.requireNonNull(title);
            Objects.requireNonNull(text);
            return new Page(icons, title, text, image);
        }
    }

    public record Icon(EntryOrTag<Item> items) {
        public static final Codec<Icon> CODEC = EntryOrTag.codec(RegistryKeys.ITEM).xmap(Icon::new, Icon::items);

        public List<ItemStack> createStacks() {
            return switch (items) {
                case EntryOrTag.OfEntry(var item) -> List.of(item.getDefaultStack());
                case EntryOrTag.OfTag(var tag) -> Registries.ITEM.getOptional(tag).map(entries -> {
                    List<ItemStack> result = new ArrayList<>(entries.size());
                    for (var entry : entries) {
                        result.add(entry.value().getDefaultStack());
                    }
                    return result;
                }).orElse(List.of());
            };
        }
    }
}

package juuxel.adorn.client.book;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.util.EntryOrTag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
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

    public record Icon(EntryOrTag<Item> items) {
        public static final Codec<Icon> CODEC = EntryOrTag.codec(RegistryKeys.ITEM).xmap(Icon::new, Icon::items);

        public List<ItemStack> createStacks() {
            return switch (items) {
                case EntryOrTag.OfEntry(var item) -> List.of(item.getDefaultStack());
                case EntryOrTag.OfTag(var tag) -> {
                    var entries = Registries.ITEM.getOrCreateEntryList(tag);
                    List<ItemStack> result = new ArrayList<>(entries.size());
                    for (var entry : entries) {
                        result.add(entry.value().getDefaultStack());
                    }
                    yield result;
                }
            };
        }
    }
}

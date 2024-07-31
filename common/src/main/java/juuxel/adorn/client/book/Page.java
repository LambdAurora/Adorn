package juuxel.adorn.client.book;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.util.MoreCodecs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Page(List<Icon> icons, Text title, Text text, @Nullable Image image) {
    public static final Codec<Page> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Icon.CODEC.listOf().fieldOf("icons").forGetter(Page::icons),
        MoreCodecs.TEXT.fieldOf("title").forGetter(Page::title),
        MoreCodecs.TEXT.optionalFieldOf("text", Text.empty()).forGetter(Page::text),
        Image.CODEC.optionalFieldOf("image").forGetter(page -> Optional.ofNullable(page.image))
    ).apply(instance, Page::new));

    // For DFU
    private Page(List<Icon> icons, Text title, Text text, Optional<Image> image) {
        this(icons, title, text, image.orElse(null));
    }

    public sealed interface Icon {
        Codec<Icon> CODEC = new Codec<>() {
            @Override
            public <T> DataResult<T> encode(Icon input, DynamicOps<T> ops, T prefix) {
                var id = switch (input) {
                    case ItemIcon(var item) -> Registries.ITEM.getId(item).toString();
                    case TagIcon(var tag) -> "#" + tag.id();
                };
                return ops.mergeToPrimitive(prefix, ops.createString(id));
            }

            @Override
            public <T> DataResult<Pair<Icon, T>> decode(DynamicOps<T> ops, T input) {
                return ops.getStringValue(input)
                    .map(id -> {
                        Icon icon;
                        if (id.startsWith("#")) {
                            icon = new TagIcon(TagKey.of(RegistryKeys.ITEM, new Identifier(id.substring(1))));
                        } else {
                            icon = new ItemIcon(Registries.ITEM.get(new Identifier(id)));
                        }

                        return Pair.of(icon, ops.empty());
                    });
            }
        };

        List<ItemStack> createStacks();

        record ItemIcon(Item item) implements Icon {
            @Override
            public List<ItemStack> createStacks() {
                return List.of(item.getDefaultStack());
            }
        }

        record TagIcon(TagKey<Item> tag) implements Icon {
            @Override
            public List<ItemStack> createStacks() {
                var entries = Registries.ITEM.getOrCreateEntryList(tag);
                List<ItemStack> result = new ArrayList<>(entries.size());
                for (var entry : entries) {
                    result.add(entry.value().getDefaultStack());
                }
                return result;
            }
        }
    }
}

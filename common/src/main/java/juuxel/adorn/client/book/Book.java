package juuxel.adorn.client.book;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.util.MoreCodecs;
import net.minecraft.text.Text;

import java.util.List;

public record Book(Text title, Text subtitle, Text author, List<Page> pages, float titleScale) {
    public static final Codec<Book> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        MoreCodecs.TEXT.fieldOf("title").forGetter(Book::title),
        MoreCodecs.TEXT.fieldOf("subtitle").forGetter(Book::subtitle),
        MoreCodecs.TEXT.fieldOf("author").forGetter(Book::author),
        Page.CODEC.listOf().fieldOf("pages").forGetter(Book::pages),
        Codec.FLOAT.fieldOf("titleScale").forGetter(Book::titleScale)
    ).apply(instance, Book::new));
}

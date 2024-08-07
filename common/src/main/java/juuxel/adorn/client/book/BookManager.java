package juuxel.adorn.client.book;

import com.google.common.base.Predicates;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import juuxel.adorn.util.Logging;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;

import java.util.Map;
import java.util.stream.Collectors;

public class BookManager extends JsonDataLoader {
    private static final Logger LOGGER = Logging.logger();
    private static final String DATA_TYPE = "adorn/books";
    private static final Gson GSON = new Gson();

    private Map<Identifier, Book> books = Map.of();

    public BookManager() {
        super(GSON, DATA_TYPE);
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        books = prepared.entrySet()
            .stream()
            .map(entry -> {
                var id = entry.getKey();
                var book = Book.CODEC.decode(JsonOps.INSTANCE, entry.getValue());
                return book.mapOrElse(
                    pair -> Pair.of(id, pair.getFirst()),
                    error -> {
                        LOGGER.error("Could not load book {}: {}", id, error.message());
                        return null;
                    }
                );
            })
            .filter(Predicates.notNull())
            .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    public boolean contains(Identifier id) {
        return books.containsKey(id);
    }

    public Book get(Identifier id) {
        var book = books.get(id);
        if (book == null) {
            throw new IllegalArgumentException("Tried to get unknown book '%s' from BookManager".formatted(id));
        }
        return book;
    }
}

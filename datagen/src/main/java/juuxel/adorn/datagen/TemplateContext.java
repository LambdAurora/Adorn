package juuxel.adorn.datagen;

import java.util.Map;
import java.util.function.Consumer;

public interface TemplateContext {
    void init(Material material);
    void set(String key, String other);

    default void set(String key, Id id) {
        setId(key, id.namespace(), id.path());
    }

    void setId(String key, String namespace, String path);

    default void setId(String key, String id) {
        set(key, Id.parse(id));
    }

    void putAll(Map<String, String> properties);

    static Map<String, String> buildSubstitutions(Consumer<TemplateContext> block) {
        var builder = new TemplateBuilder();
        block.accept(builder);
        return builder.build();
    }
}

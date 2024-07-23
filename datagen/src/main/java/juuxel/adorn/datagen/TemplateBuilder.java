package juuxel.adorn.datagen;

import java.util.HashMap;
import java.util.Map;

final class TemplateBuilder implements TemplateContext {
    private final Map<String, String> substitutions = new HashMap<>();

    @Override
    public void init(Material material) {
        set("id", material.getId());
        set("mod-id", material.isModded() ? material.getId().namespace() : "");
        set("mod-prefix", material.isModded() ? material.getId().namespace() + "/" : "");
        set("stick", material.getStick());
        material.appendTemplates(this);
    }

    @Override
    public void set(String key, String other) {
        substitutions.put(key, other);
    }

    @Override
    public void setId(String key, String namespace, String path) {
        substitutions.put(key, namespace + ':' + path);
        substitutions.put(key + ".namespace", namespace);
        substitutions.put(key + ".path", path);
    }

    @Override
    public void putAll(Map<String, String> properties) {
        substitutions.putAll(properties);
    }

    private String resolve(String key) {
        var current = substitutions.get(key);

        while (current.indexOf('<') >= 0) {
            current = TemplateApplier.apply(current, substitutions);
        }

        return current;
    }

    public Map<String, String> build() {
        Map<String, String> result = new HashMap<>();

        for (String key : substitutions.keySet()) {
            result.put(key, resolve(key));
        }

        return result;
    }
}

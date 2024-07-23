package juuxel.adorn.datagen;

import java.util.Map;

public final class TemplateApplier {
    public static String apply(String text, Map<String, String> substitutions) {
        for (var entry : substitutions.entrySet()) {
            text = text.replace("<" + entry.getKey() + ">", entry.getValue());
        }

        return text;
    }
}

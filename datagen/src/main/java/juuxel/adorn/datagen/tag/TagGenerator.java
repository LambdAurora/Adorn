package juuxel.adorn.datagen.tag;

import juuxel.adorn.datagen.DataOutput;
import juuxel.adorn.datagen.GeneratorConfig;
import juuxel.adorn.datagen.GeneratorConfigLoader;
import juuxel.adorn.datagen.Id;
import juuxel.adorn.datagen.Material;
import juuxel.adorn.datagen.util.DataUtil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public final class TagGenerator {
    public static final Map<Id, TagGenerator> GENERATORS_BY_ID = Map.ofEntries(
        Map.entry(adorn("benches"), new TagGenerator(TagEntryProviders.BENCHES)),
        Map.entry(adorn("chairs"), new TagGenerator(TagEntryProviders.CHAIRS)),
        Map.entry(adorn("coffee_tables"), new TagGenerator(TagEntryProviders.COFFEE_TABLES)),
        Map.entry(adorn("drawers"), new TagGenerator(TagEntryProviders.DRAWERS)),
        Map.entry(adorn("dyed_candlelit_lanterns"), new TagGenerator(TagEntryProviders.DYED_CANDLELIT_LANTERNS)),
        Map.entry(adorn("kitchen_counters"), new TagGenerator(TagEntryProviders.KITCHEN_COUNTERS)),
        Map.entry(adorn("kitchen_cupboards"), new TagGenerator(TagEntryProviders.KITCHEN_CUPBOARDS)),
        Map.entry(adorn("kitchen_sinks"), new TagGenerator(TagEntryProviders.KITCHEN_SINKS)),
        Map.entry(adorn("sofas"), new TagGenerator(TagEntryProviders.SOFAS)),
        Map.entry(adorn("stone_platforms"), new TagGenerator(TagEntryProviders.STONE_PLATFORMS)),
        Map.entry(adorn("stone_posts"), new TagGenerator(TagEntryProviders.STONE_POSTS)),
        Map.entry(adorn("stone_steps"), new TagGenerator(TagEntryProviders.STONE_STEPS)),
        Map.entry(adorn("tables"), new TagGenerator(TagEntryProviders.TABLES)),
        Map.entry(adorn("table_lamps"), new TagGenerator(TagEntryProviders.TABLE_LAMPS)),
        Map.entry(adorn("wooden_platforms"), new TagGenerator(TagEntryProviders.WOODEN_PLATFORMS)),
        Map.entry(adorn("wooden_posts"), new TagGenerator(TagEntryProviders.WOODEN_POSTS)),
        Map.entry(adorn("wooden_shelves"), new TagGenerator(TagEntryProviders.WOODEN_SHELVES)),
        Map.entry(adorn("wooden_steps"), new TagGenerator(TagEntryProviders.WOODEN_STEPS)),
        Map.entry(minecraft("non_flammable_wood"), new TagGenerator(TagEntryProviders.NON_FLAMMABLE_WOOD))
    );

    private final TagEntryProvider entries;

    public TagGenerator(TagEntryProvider entries) {
        this.entries = entries;
    }

    public String generate(List<Material> materials) {
        var entries = this.entries.getEntries(materials);
        var joiner = new StringJoiner(",\n    ");
        for (var entry : entries) {
            if (entry.isModded()) {
                joiner.add("{ \"id\": \"%s\", \"required\": false }".formatted(entry.id()));
            } else {
                joiner.add("\"%s\"".formatted(entry.id()));
            }
        }
        return """
            {
              "replace": false,
              "values": [
                %s
              ]
            }
            """.formatted(joiner);
    }

    public static void generateFromConfigFiles(List<Path> configFiles, DataOutput output) {
        var configs = configFiles.stream()
            .sorted(Comparator.comparing(Path::toAbsolutePath))
            .map(configFile -> {
                try {
                    return GeneratorConfigLoader.read(configFile);
                } catch (IOException e) {
                    throw new UncheckedIOException("Could not read config file " + configFile, e);
                }
            })
            .toList();
        generate(configs, output);
    }

    public static void generate(List<GeneratorConfig> configs, DataOutput output) {
        var materials = configs.stream()
            .<GeneratorConfig.MaterialEntry<?>>mapMulti((config, sink) -> {
                config.woods().forEach(sink);
                config.stones().forEach(sink);
                config.wools().forEach(sink);
            })
            .<Material>map(GeneratorConfig.MaterialEntry::material)
            .filter(DataUtil.distinctBy(Material::getId))
            .toList();

        GENERATORS_BY_ID.forEach((id, generator) -> {
            for (var tagType : new String[] { "blocks", "items" }) {
                var path = "data/%s/tags/%s/%s.json".formatted(id.namespace(), tagType, id.path());
                output.write(path, generator.generate(materials));
            }
        });
    }

    private static Id adorn(String path) {
        return new Id("adorn", path);
    }

    private static Id minecraft(String path) {
        return new Id("minecraft", path);
    }
}

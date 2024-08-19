package juuxel.adorn.datagen.tag;

import juuxel.adorn.datagen.ColorMaterial;
import juuxel.adorn.datagen.DataOutput;
import juuxel.adorn.datagen.GeneratorConfig;
import juuxel.adorn.datagen.GeneratorConfigLoader;
import juuxel.adorn.datagen.Id;
import juuxel.adorn.datagen.Material;
import juuxel.adorn.datagen.SimpleWoodMaterial;
import juuxel.adorn.datagen.util.DataUtil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public final class TagGenerator {
    public static final List<TagGenerator> GENERATORS = List.of(
        new TagGenerator(adorn("benches"), TagEntryProviders.BENCHES),
        new TagGenerator(adorn("chairs"), TagEntryProviders.CHAIRS),
        new TagGenerator(adorn("coffee_tables"), TagEntryProviders.COFFEE_TABLES),
        new TagGenerator(adorn("drawers"), TagEntryProviders.DRAWERS),
        new TagGenerator(adorn("dyed_candlelit_lanterns"), TagEntryProviders.DYED_CANDLELIT_LANTERNS),
        new TagGenerator(adorn("kitchen_counters"), TagEntryProviders.KITCHEN_COUNTERS),
        new TagGenerator(adorn("kitchen_cupboards"), TagEntryProviders.KITCHEN_CUPBOARDS),
        new TagGenerator(adorn("kitchen_sinks"), TagEntryProviders.KITCHEN_SINKS),
        new TagGenerator(adorn("sofas"), TagEntryProviders.SOFAS),
        new TagGenerator(adorn("stone_platforms"), TagEntryProviders.STONE_PLATFORMS),
        new TagGenerator(adorn("stone_posts"), TagEntryProviders.STONE_POSTS),
        new TagGenerator(adorn("stone_steps"), TagEntryProviders.STONE_STEPS),
        new TagGenerator(adorn("tables"), TagEntryProviders.TABLES),
        new TagGenerator(adorn("table_lamps"), TagEntryProviders.TABLE_LAMPS),
        new TagGenerator(adorn("wooden_platforms"), TagEntryProviders.WOODEN_PLATFORMS),
        new TagGenerator(adorn("wooden_posts"), TagEntryProviders.WOODEN_POSTS),
        new TagGenerator(adorn("wooden_shelves"), TagEntryProviders.WOODEN_SHELVES),
        new TagGenerator(adorn("wooden_steps"), TagEntryProviders.WOODEN_STEPS),
        new TagGenerator(minecraft("non_flammable_wood"), TagEntryProviders.NON_FLAMMABLE_WOOD)
    );

    private final Id id;
    private final TagEntryProvider entries;

    public TagGenerator(Id id, TagEntryProvider entries) {
        this.id = id;
        this.entries = entries;
    }

    public String generate(List<Material> materials) {
        var entries = this.entries.getEntries(materials);
        var joiner = new StringJoiner(",\n    ");

        var entryMaterials = entries.stream().map(TagEntryProvider.Entry::material).collect(Collectors.toSet());
        // The first check is for excluding tags with wool-based blocks.
        boolean hasAllPaintedWoods = entryMaterials.stream().anyMatch(material -> material instanceof SimpleWoodMaterial) &&
            entryMaterials.containsAll(Arrays.asList(ColorMaterial.values()));
        boolean encounteredColor = false;

        for (var entry : entries) {
            if (hasAllPaintedWoods && entry.material() instanceof ColorMaterial) {
                if (!encounteredColor) {
                    joiner.add("\"#%s\"".formatted(id.prefixed("painted").toString().replace("wooden", "wood")));
                    encounteredColor = true;
                }

                continue;
            }

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
                config.colors().forEach(sink);
            })
            .<Material>map(GeneratorConfig.MaterialEntry::material)
            .filter(DataUtil.distinctBy(Material::getId))
            .toList();

        for (var generator : GENERATORS) {
            for (var tagType : new String[] { "block", "item" }) {
                var id = generator.id;
                var path = "data/%s/tags/%s/%s.json".formatted(id.namespace(), tagType, id.path());
                output.write(path, generator.generate(materials));
            }
        }
    }

    private static Id adorn(String path) {
        return new Id("adorn", path);
    }

    private static Id minecraft(String path) {
        return new Id("minecraft", path);
    }
}

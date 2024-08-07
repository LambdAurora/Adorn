package juuxel.adorn.client.resources;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.util.Colors;
import juuxel.adorn.util.Logging;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ColorManager extends SinglePreparationResourceReloader<Map<Identifier, List<JsonObject>>> {
    private static final Logger LOGGER = Logging.logger();
    private static final Identifier FALLBACK = AdornCommon.id("fallback");
    private static final String PREFIX = "adorn/color_palettes";
    private static final Pattern COLOR_REGEX = Pattern.compile("^#(?:[0-9A-Fa-f]{2})?[0-9A-Fa-f]{6}$");
    private static final Codec<Map<Identifier, ColorPair>> PALETTE_CODEC = Codec.unboundedMap(Identifier.CODEC, ColorPair.CODEC);

    private final Map<Identifier, ColorPalette> palettes = new HashMap<>();

    @Override
    protected Map<Identifier, List<JsonObject>> prepare(ResourceManager manager, Profiler profiler) {
        var gson = new Gson();
        var resourceFinder = ResourceFinder.json(PREFIX);
        Map<Identifier, List<JsonObject>> result = new HashMap<>();

        for (var entry : resourceFinder.findAllResources(manager).entrySet()) {
            var id = resourceFinder.toResourceId(entry.getKey());
            List<JsonObject> jsons = new ArrayList<>();
            result.put(id, jsons);

            for (var resource : entry.getValue()) {
                try (var in = resource.getReader()) {
                    jsons.add(gson.fromJson(in, JsonObject.class));
                } catch (IOException | JsonParseException e) {
                    LOGGER.error("[Adorn] Could not load color palette resource {} from {}", entry.getKey(), resource.getPackId(), e);
                }
            }
        }

        return result;
    }

    @Override
    protected void apply(Map<Identifier, List<JsonObject>> prepared, ResourceManager manager, Profiler profiler) {
        palettes.clear();
        prepared.forEach((id, jsons) -> {
            var palette = new HashMap<Identifier, ColorPair>();

            for (var json : jsons) {
                switch (PALETTE_CODEC.parse(JsonOps.INSTANCE, json)) {
                    case DataResult.Success(Map<Identifier, ColorPair> partialPalette, Lifecycle lifecycle) -> palette.putAll(partialPalette);
                    case DataResult.Error<?> error -> LOGGER.error("[Adorn] Could not parse color palette {}: {}", id, error.message());
                }
            }

            palettes.put(id, new ColorPalette(palette));
        });
    }

    public ColorPalette getColors(Identifier id) {
        var pair = palettes.get(id);
        if (pair == null) {
            pair = palettes.get(FALLBACK);
            if (pair == null) {
                throw new IllegalStateException("Could not find fallback palette!");
            }
        }
        return pair;
    }

    private static DataResult<Integer> parseHexColor(String str) {
        if (!COLOR_REGEX.matcher(str).matches()) {
            return DataResult.error(() -> "Color must be a hex color beginning with '#' - found " + str);
        }

        var colorStr = str.substring(1);
        return DataResult.success(switch (colorStr.length()) {
            case 6 -> Colors.color(Integer.parseInt(colorStr, 16));
            case 8 -> Integer.parseInt(colorStr, 16);
            default -> throw new MatchException("Mismatching color length: " + colorStr.length(), null);
        });
    }

    public record ColorPair(int bg, int fg) {
        private static final int DEFAULT_FG = Colors.SCREEN_TEXT;

        private static final Codec<Integer> COLOR_CODEC =
            Codec.STRING.comapFlatMap(ColorManager::parseHexColor, color -> '#' + HexFormat.of().withUpperCase().toHexDigits(color));
        public static final Codec<ColorPair> CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(instance -> instance.group(
                COLOR_CODEC.fieldOf("bg").forGetter(ColorPair::bg),
                COLOR_CODEC.optionalFieldOf("fg", DEFAULT_FG).forGetter(ColorPair::bg)
            ).apply(instance, ColorPair::new)),
            COLOR_CODEC.xmap(bg -> new ColorPair(bg, DEFAULT_FG), ColorPair::bg)
        );
    }
}

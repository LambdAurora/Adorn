package juuxel.adorn.lib;

import juuxel.adorn.AdornCommon;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class AdornTags {
    public static final TagPair CHAIRS = blockAndItem("chairs");
    public static final TagPair TABLES = blockAndItem("tables");
    public static final TagPair DRAWERS = blockAndItem("drawers");
    public static final TagPair BENCHES = blockAndItem("benches");
    public static final TagPair KITCHEN_COUNTERS = blockAndItem("kitchen_counters");
    public static final TagPair KITCHEN_CUPBOARDS = blockAndItem("kitchen_cupboards");
    public static final TagPair KITCHEN_SINKS = blockAndItem("kitchen_sinks");
    public static final TagPair KITCHEN_BLOCKS = blockAndItem("kitchen_blocks");
    public static final TagPair SOFAS = blockAndItem("sofas");
    public static final TagPair POSTS = blockAndItem("posts");
    public static final TagPair PLATFORMS = blockAndItem("platforms");
    public static final TagPair STEPS = blockAndItem("steps");
    public static final TagPair WOODEN_POSTS = blockAndItem("wooden_posts");
    public static final TagPair WOODEN_PLATFORMS = blockAndItem("wooden_platforms");
    public static final TagPair WOODEN_STEPS = blockAndItem("wooden_steps");
    public static final TagPair STONE_POSTS = blockAndItem("stone_posts");
    public static final TagPair STONE_PLATFORMS = blockAndItem("stone_platforms");
    public static final TagPair STONE_STEPS = blockAndItem("stone_steps");
    public static final TagPair SHELVES = blockAndItem("shelves");
    public static final TagPair WOODEN_SHELVES = blockAndItem("wooden_shelves");
    public static final TagPair CHIMNEYS = blockAndItem("chimneys");
    public static final TagPair CRATES = blockAndItem("crates");
    public static final TagPair COFFEE_TABLES = blockAndItem("coffee_tables");
    public static final TagPair TABLE_LAMPS = blockAndItem("table_lamps");
    public static final TagPair CANDLELIT_LANTERNS = blockAndItem("candlelit_lanterns");
    public static final TagPair COPPER_PIPES = blockAndItem("copper_pipes");
    public static final TagKey<Block> COPPER_PIPES_CONNECT_TO = block("copper_pipes_connect_to");
    public static final TagKey<Item> WATERING_CAN_FERTILIZERS = item("watering_can_fertilizers");

    public static void init() {
    }

    private static TagKey<Block> block(String path) {
        return TagKey.of(RegistryKeys.BLOCK, AdornCommon.id(path));
    }

    private static TagKey<Item> item(String path) {
        return TagKey.of(RegistryKeys.ITEM, AdornCommon.id(path));
    }

    private static TagPair blockAndItem(String path) {
        return new TagPair(block(path), item(path));
    }

    public record TagPair(TagKey<Block> block, TagKey<Item> item) {
    }
}

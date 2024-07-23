package juuxel.adorn.datagen.tag;

import juuxel.adorn.datagen.ColorMaterial;
import juuxel.adorn.datagen.StoneMaterial;
import juuxel.adorn.datagen.WoodMaterial;

public final class TagEntryProviders {
    public static final TagEntryProvider BENCHES = wood("bench");
    public static final TagEntryProvider CHAIRS = wood("chair");
    public static final TagEntryProvider COFFEE_TABLES = wood("coffee_table");
    public static final TagEntryProvider DRAWERS = wood("drawer");
    public static final TagEntryProvider DYED_CANDLELIT_LANTERNS = wool("candlelit_lantern");
    public static final TagEntryProvider KITCHEN_COUNTERS = wood("kitchen_counter");
    public static final TagEntryProvider KITCHEN_CUPBOARDS = wood("kitchen_cupboard");
    public static final TagEntryProvider KITCHEN_SINKS = wood("kitchen_sink");
    public static final TagEntryProvider SOFAS = wool("sofa");
    public static final TagEntryProvider STONE_PLATFORMS = stone("platform");
    public static final TagEntryProvider STONE_POSTS = stone("post");
    public static final TagEntryProvider STONE_STEPS = stone("step");
    public static final TagEntryProvider TABLES = wood("table");
    public static final TagEntryProvider TABLE_LAMPS = wool("table_lamp");
    public static final TagEntryProvider WOODEN_PLATFORMS = wood("platform");
    public static final TagEntryProvider WOODEN_POSTS = wood("post");
    public static final TagEntryProvider WOODEN_SHELVES = wood("shelf");
    public static final TagEntryProvider WOODEN_STEPS = wood("step");
    public static final TagEntryProvider NON_FLAMMABLE_WOOD = new TagEntryProvider.Filtered(
        new TagEntryProvider.Multi(
            BENCHES,
            CHAIRS,
            COFFEE_TABLES,
            DRAWERS,
            KITCHEN_COUNTERS,
            KITCHEN_CUPBOARDS,
            KITCHEN_SINKS,
            TABLES,
            WOODEN_PLATFORMS,
            WOODEN_POSTS,
            WOODEN_SHELVES,
            WOODEN_STEPS
        ),
        material -> material instanceof WoodMaterial wood && wood.isNonFlammable()
    );

    private static TagEntryProvider wood(String blockType) {
        return new TagEntryProvider.Filtered(
            new TagEntryProvider.Simple(blockType),
            material -> material instanceof WoodMaterial
        );
    }

    private static TagEntryProvider stone(String blockType) {
        return new TagEntryProvider.Filtered(
            new TagEntryProvider.Simple(blockType),
            material -> material instanceof StoneMaterial
        );
    }

    private static TagEntryProvider wool(String blockType) {
        return new TagEntryProvider.Filtered(
            new TagEntryProvider.Simple(blockType),
            material -> material instanceof ColorMaterial
        );
    }
}

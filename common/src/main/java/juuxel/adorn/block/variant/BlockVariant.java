package juuxel.adorn.block.variant;

import juuxel.adorn.util.AdornUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public interface BlockVariant {
    char MOD_ID_SEPARATOR = '/';

    Map<DyeColor, BlockVariant> WOOLS = Collections.unmodifiableMap(
        new EnumMap<DyeColor, BlockVariant>(
            Arrays.stream(DyeColor.values())
                .map(color -> Map.entry(color, variant(color.asString(), Blocks.WHITE_WOOL)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        )
    );

    BlockVariant IRON = variant("iron", Blocks.IRON_BARS);
    BlockVariant OAK = variant("oak", Blocks.OAK_PLANKS);
    BlockVariant SPRUCE = variant("spruce", Blocks.SPRUCE_PLANKS);
    BlockVariant BIRCH = variant("birch", Blocks.BIRCH_PLANKS);
    BlockVariant JUNGLE = variant("jungle", Blocks.JUNGLE_PLANKS);
    BlockVariant ACACIA = variant("acacia", Blocks.ACACIA_PLANKS);
    BlockVariant DARK_OAK = variant("dark_oak", Blocks.DARK_OAK_PLANKS);
    BlockVariant MANGROVE = variant("mangrove", Blocks.MANGROVE_PLANKS);
    BlockVariant CHERRY = variant("cherry", Blocks.CHERRY_PLANKS);
    BlockVariant BAMBOO = variant("bamboo", Blocks.BAMBOO_PLANKS);
    BlockVariant CRIMSON = variant("crimson", Blocks.CRIMSON_PLANKS);
    BlockVariant WARPED = variant("warped", Blocks.WARPED_PLANKS);
    BlockVariant STONE = variant("stone", Blocks.STONE);
    BlockVariant COBBLESTONE = variant("cobblestone", Blocks.COBBLESTONE);
    BlockVariant SANDSTONE = variant("sandstone", Blocks.SANDSTONE);
    BlockVariant DIORITE = variant("diorite", Blocks.DIORITE);
    BlockVariant ANDESITE = variant("andesite", Blocks.ANDESITE);
    BlockVariant GRANITE = variant("granite", Blocks.GRANITE);
    BlockVariant BRICK = variant("brick", Blocks.BRICKS);
    BlockVariant STONE_BRICK = variant("stone_brick", Blocks.STONE_BRICKS);
    BlockVariant RED_SANDSTONE = variant("red_sandstone", Blocks.RED_SANDSTONE);
    BlockVariant NETHER_BRICK = variant("nether_brick", Blocks.NETHER_BRICKS);
    BlockVariant BASALT = variant("basalt", Blocks.BASALT);
    BlockVariant BLACKSTONE = variant("blackstone", Blocks.BLACKSTONE);
    BlockVariant RED_NETHER_BRICK = variant("red_nether_brick", Blocks.RED_NETHER_BRICKS);
    BlockVariant PRISMARINE = variant("prismarine", Blocks.PRISMARINE);
    BlockVariant QUARTZ = variant("quartz", Blocks.QUARTZ_BLOCK);
    BlockVariant END_STONE_BRICK = variant("end_stone_brick", Blocks.END_STONE_BRICKS);
    BlockVariant PURPUR = variant("purpur", Blocks.PURPUR_BLOCK);
    BlockVariant POLISHED_BLACKSTONE = variant("polished_blackstone", Blocks.POLISHED_BLACKSTONE);
    BlockVariant POLISHED_BLACKSTONE_BRICK = variant("polished_blackstone_brick", Blocks.POLISHED_BLACKSTONE_BRICKS);
    BlockVariant POLISHED_DIORITE = variant("polished_diorite", Blocks.POLISHED_DIORITE);
    BlockVariant POLISHED_ANDESITE = variant("polished_andesite", Blocks.POLISHED_ANDESITE);
    BlockVariant POLISHED_GRANITE = variant("polished_granite", Blocks.POLISHED_GRANITE);
    BlockVariant PRISMARINE_BRICK = variant("prismarine_brick", Blocks.PRISMARINE_BRICKS);
    BlockVariant DARK_PRISMARINE = variant("dark_prismarine", Blocks.DARK_PRISMARINE);
    BlockVariant CUT_SANDSTONE = variant("cut_sandstone", Blocks.CUT_SANDSTONE);
    BlockVariant SMOOTH_SANDSTONE = variant("smooth_sandstone", Blocks.SMOOTH_SANDSTONE);
    BlockVariant CUT_RED_SANDSTONE = variant("cut_red_sandstone", Blocks.CUT_RED_SANDSTONE);
    BlockVariant SMOOTH_RED_SANDSTONE = variant("smooth_red_sandstone", Blocks.SMOOTH_RED_SANDSTONE);
    BlockVariant SMOOTH_STONE = variant("smooth_stone", Blocks.SMOOTH_STONE);
    BlockVariant MOSSY_COBBLESTONE = variant("mossy_cobblestone", Blocks.MOSSY_COBBLESTONE);
    BlockVariant MOSSY_STONE_BRICK = variant("mossy_stone_brick", Blocks.MOSSY_STONE_BRICKS);
    BlockVariant DEEPSLATE = variant("deepslate", Blocks.DEEPSLATE);
    BlockVariant COBBLED_DEEPSLATE = variant("cobbled_deepslate", Blocks.COBBLED_DEEPSLATE);
    BlockVariant POLISHED_DEEPSLATE = variant("polished_deepslate", Blocks.POLISHED_DEEPSLATE);
    BlockVariant DEEPSLATE_BRICK = variant("deepslate_brick", Blocks.DEEPSLATE_BRICKS);
    BlockVariant DEEPSLATE_TILE = variant("deepslate_tile", Blocks.DEEPSLATE_TILES);

    /**
     * The name of this variant. Must be a valid identifier path.
     */
    String name();

    default Identifier nameAsIdentifier() {
        var name = this.name();
        return Identifier.splitOn(name, MOD_ID_SEPARATOR);
    }

    /**
     * Creates a <em>new</em> {@code AbstractBlock.Settings}.
     */
    AbstractBlock.Settings createSettings();

    static BlockVariant variant(String name, Block base) {
        return new BlockVariant() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public AbstractBlock.Settings createSettings() {
                return AdornUtil.copySettingsSafely(base);
            }
        };
    }

    static BlockVariant wool(DyeColor color) {
        return WOOLS.get(color);
    }

    record Wood(String name) implements BlockVariant {
        @Override
        public AbstractBlock.Settings createSettings() {
            return AdornUtil.copySettingsSafely(Blocks.OAK_PLANKS);
        }
    }

    record Stone(String name) implements BlockVariant {
        @Override
        public AbstractBlock.Settings createSettings() {
            return AdornUtil.copySettingsSafely(Blocks.COBBLESTONE);
        }
    }
}

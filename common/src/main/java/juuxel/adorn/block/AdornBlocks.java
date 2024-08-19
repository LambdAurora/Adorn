package juuxel.adorn.block;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.item.TradingStationItem;
import juuxel.adorn.lib.AdornSounds;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.lib.registry.RegistryHelper;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.util.AdornUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;

import java.util.Map;

public final class AdornBlocks {
    public static final Registrar<Block> BLOCKS = RegistrarFactory.get().create(RegistryKeys.BLOCK);
    public static final Registrar<Item> ITEMS = RegistrarFactory.get().create(RegistryKeys.ITEM);
    private static final RegistryHelper HELPER = new RegistryHelper(BLOCKS, ITEMS);

    public static final Registered<Map<DyeColor, SofaBlock>> SOFAS = AdornUtil.associateLazily(
        DyeColor.values(),
        color -> HELPER.registerBlock(
            color.asString() + "_sofa",
            () -> PlatformBridges.get().getBlockFactory().createSofa(BlockVariant.wool(color))
        )
    );

    public static final Registered<Map<DyeColor, Block>> PAINTED_PLANKS = AdornUtil.associateLazily(
        DyeColor.values(),
        color -> HELPER.registerBlock(
            color.asString() + "_planks",
            () -> PlatformBridges.get().getBlockFactory().createPaintedPlanks(BlockVariant.OAK.createSettings().mapColor(color))
        )
    );

    public static final Registered<Map<DyeColor, Block>> PAINTED_WOOD_SLABS = AdornUtil.associateLazily(
        DyeColor.values(),
        color -> HELPER.registerBlock(
            color.asString() + "_wood_slab",
            () -> PlatformBridges.get().getBlockFactory().createPaintedWoodSlab(BlockVariant.OAK.createSettings().mapColor(color))
        )
    );

    public static final Registered<Map<DyeColor, Block>> PAINTED_WOOD_STAIRS = AdornUtil.associateLazily(
        DyeColor.values(),
        color -> {
            var planks = PAINTED_PLANKS.get().get(color);
            return HELPER.registerBlock(
                color.asString() + "_wood_stairs",
                () -> PlatformBridges.get().getBlockFactory().createPaintedWoodStairs(planks.getDefaultState(), BlockVariant.OAK.createSettings().mapColor(color))
            );
        }
    );

    public static final Registered<Block> BRICK_CHIMNEY = HELPER.registerBlock("brick_chimney",
        () -> new ChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.RED)));
    public static final Registered<Block> STONE_BRICK_CHIMNEY = HELPER.registerBlock("stone_brick_chimney",
        () -> new ChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.STONE_GRAY)));
    public static final Registered<Block> NETHER_BRICK_CHIMNEY = HELPER.registerBlock("nether_brick_chimney",
        () -> new ChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.DARK_RED)));
    public static final Registered<Block> RED_NETHER_BRICK_CHIMNEY = HELPER.registerBlock("red_nether_brick_chimney",
        () -> new ChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.DARK_RED)));
    public static final Registered<Block> COBBLESTONE_CHIMNEY = HELPER.registerBlock("cobblestone_chimney",
        () -> new ChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.STONE_GRAY)));
    public static final Registered<Block> PRISMARINE_CHIMNEY = HELPER.registerBlock("prismarine_chimney",
        () -> new PrismarineChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.CYAN, 1.5f)));
    public static final Registered<Block> MAGMATIC_PRISMARINE_CHIMNEY = HELPER.registerBlock("magmatic_prismarine_chimney",
        () -> new PrismarineChimneyBlock.WithColumn(true, AbstractChimneyBlock.createBlockSettings(MapColor.CYAN, 1.5f).luminance(state -> 3)));
    public static final Registered<Block> SOULFUL_PRISMARINE_CHIMNEY = HELPER.registerBlock("soulful_prismarine_chimney",
        () -> new PrismarineChimneyBlock.WithColumn(false, AbstractChimneyBlock.createBlockSettings(MapColor.CYAN, 1.5f)));

    public static final Registered<Map<DyeColor, Block>> TABLE_LAMPS = AdornUtil.associateLazily(
        DyeColor.values(),
        color -> HELPER.registerBlock(color.asString() + "_table_lamp", () -> new TableLampBlock(TableLampBlock.createBlockSettings(color)))
    );

    public static final Registered<Block> TRADING_STATION = HELPER.registerBlock(
        "trading_station",
        block -> new TradingStationItem(block, new Item.Settings()),
        () -> new TradingStationBlock(AbstractBlock.Settings.create().mapColor(MapColor.GREEN).strength(2.5f).sounds(BlockSoundGroup.WOOD))
    );

    public static final Registered<Block> STONE_TORCH_GROUND = HELPER.registerBlockWithoutItem("stone_torch",
        () -> new TorchBlock(
            ParticleTypes.FLAME,
            AbstractBlock.Settings.copy(Blocks.TORCH)
                .sounds(BlockSoundGroup.STONE)
                .luminance(state -> 15)
        ));

    public static final Registered<Block> STONE_TORCH_WALL = HELPER.registerBlockWithoutItem("wall_stone_torch",
        () -> new WallTorchBlock(
            ParticleTypes.FLAME,
            AbstractBlock.Settings.copy(STONE_TORCH_GROUND.get()).dropsLike(STONE_TORCH_GROUND.get())
        ));

    public static final Registered<Block> CRATE = HELPER.registerBlock("crate",
        () -> new Block(AdornUtil.copySettingsSafely(Blocks.OAK_PLANKS)));
    public static final Registered<Block> APPLE_CRATE = registerCrate("apple_crate");
    public static final Registered<Block> WHEAT_CRATE = registerCrate("wheat_crate");
    public static final Registered<Block> CARROT_CRATE = registerCrate("carrot_crate");
    public static final Registered<Block> POTATO_CRATE = registerCrate("potato_crate");
    public static final Registered<Block> MELON_CRATE = registerCrate("melon_crate");
    public static final Registered<Block> WHEAT_SEED_CRATE = registerCrate("wheat_seed_crate");
    public static final Registered<Block> MELON_SEED_CRATE = registerCrate("melon_seed_crate");
    public static final Registered<Block> PUMPKIN_SEED_CRATE = registerCrate("pumpkin_seed_crate");
    public static final Registered<Block> BEETROOT_CRATE = registerCrate("beetroot_crate");
    public static final Registered<Block> BEETROOT_SEED_CRATE = registerCrate("beetroot_seed_crate");
    public static final Registered<Block> SWEET_BERRY_CRATE = registerCrate("sweet_berry_crate");
    public static final Registered<Block> COCOA_BEAN_CRATE = registerCrate("cocoa_bean_crate");
    public static final Registered<Block> NETHER_WART_CRATE = registerCrate("nether_wart_crate");
    public static final Registered<Block> SUGAR_CANE_CRATE = registerCrate("sugar_cane_crate");
    public static final Registered<Block> EGG_CRATE = registerCrate("egg_crate");
    public static final Registered<Block> HONEYCOMB_CRATE = registerCrate("honeycomb_crate");
    public static final Registered<Block> LIL_TATER_CRATE = registerCrate("lil_tater_crate");

    public static final Registered<Block> PICKET_FENCE = HELPER.registerBlock("picket_fence",
        () -> new PicketFenceBlock(AbstractBlock.Settings.copy(Blocks.OAK_FENCE).nonOpaque()));
    public static final Registered<Block> CHAIN_LINK_FENCE = HELPER.registerBlock("chain_link_fence",
        () -> new ChainLinkFenceBlock(
            AbstractBlock.Settings.copy(Blocks.IRON_BARS)
                .sounds(AdornSounds.CHAIN_LINK_FENCE)
        ));
    public static final Registered<Block> STONE_LADDER = HELPER.registerBlock("stone_ladder",
        () -> new StoneLadderBlock(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque()));
    public static final Registered<Block> BREWER = HELPER.registerBlock("brewer",
        () -> new BrewerBlock(
            AbstractBlock.Settings.create()
                .mapColor(MapColor.DEEPSLATE_GRAY)
                .solid()
                .strength(0.8F)
                .requiresTool()
        ));

    public static final Registered<Block> CANDLELIT_LANTERN = HELPER.registerBlock("candlelit_lantern",
        () -> new CandlelitLanternBlock(CandlelitLanternBlock.createBlockSettings()));
    public static final Registered<Map<DyeColor, Block>> DYED_CANDLELIT_LANTERNS = AdornUtil.associateLazily(
        DyeColor.values(),
        color -> HELPER.registerBlock(
            color.asString() + "_candlelit_lantern",
            () -> new CandlelitLanternBlock(CandlelitLanternBlock.createBlockSettings())
        )
    );

    public static final Registered<Block> COPPER_PIPE = HELPER.registerBlock("copper_pipe",
        () -> new OxidizableCopperPipeBlock(
            Oxidizable.OxidationLevel.UNAFFECTED,
            AbstractBlock.Settings.create()
                .requiresTool()
                .strength(3f, 5f)
                .sounds(BlockSoundGroup.COPPER)
                .mapColor(MapColor.ORANGE)
        ));
    public static final Registered<Block> EXPOSED_COPPER_PIPE = HELPER.registerBlock("exposed_copper_pipe",
        () -> new OxidizableCopperPipeBlock(
            Oxidizable.OxidationLevel.EXPOSED,
            AbstractBlock.Settings.create()
                .requiresTool()
                .strength(3f, 5f)
                .sounds(BlockSoundGroup.COPPER)
                .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
        ));
    public static final Registered<Block> WEATHERED_COPPER_PIPE = HELPER.registerBlock("weathered_copper_pipe",
        () -> new OxidizableCopperPipeBlock(
            Oxidizable.OxidationLevel.WEATHERED,
            AbstractBlock.Settings.create()
                .requiresTool()
                .strength(3f, 5f)
                .sounds(BlockSoundGroup.COPPER)
                .mapColor(MapColor.DARK_AQUA)
        ));
    public static final Registered<Block> OXIDIZED_COPPER_PIPE = HELPER.registerBlock("oxidized_copper_pipe",
        () -> new OxidizableCopperPipeBlock(
            Oxidizable.OxidationLevel.OXIDIZED,
            AbstractBlock.Settings.create()
                .requiresTool()
                .strength(3f, 5f)
                .sounds(BlockSoundGroup.COPPER)
                .mapColor(MapColor.TEAL)
        ));
    public static final Registered<Block> WAXED_COPPER_PIPE = HELPER.registerBlock("waxed_copper_pipe",
        () -> new CopperPipeBlock(AbstractBlock.Settings.copy(COPPER_PIPE.get())));
    public static final Registered<Block> WAXED_EXPOSED_COPPER_PIPE = HELPER.registerBlock("waxed_exposed_copper_pipe",
        () -> new CopperPipeBlock(AbstractBlock.Settings.copy(EXPOSED_COPPER_PIPE.get())));
    public static final Registered<Block> WAXED_WEATHERED_COPPER_PIPE = HELPER.registerBlock("waxed_weathered_copper_pipe",
        () -> new CopperPipeBlock(AbstractBlock.Settings.copy(WEATHERED_COPPER_PIPE.get())));
    public static final Registered<Block> WAXED_OXIDIZED_COPPER_PIPE = HELPER.registerBlock("waxed_oxidized_copper_pipe",
        () -> new CopperPipeBlock(AbstractBlock.Settings.copy(OXIDIZED_COPPER_PIPE.get())));

    public static void init() {
    }

    private static Registered<Block> registerCrate(String name) {
        return HELPER.registerBlock(name, () -> new Item.Settings().recipeRemainder(CRATE.get().asItem()), () -> new Block(AdornUtil.copySettingsSafely(CRATE.get())));
    }
}

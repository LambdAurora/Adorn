package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;

import java.util.Arrays;
import java.util.List;

public final class BygCompat implements BlockVariantSet {
    private static final String[] WOOD_VARIANTS = {
        "aspen",
        "baobab",
        "blue_enchanted",
        "bulbis",
        "cherry",
        "cika",
        "cypress",
        "ebony",
        "embur",
        "ether",
        "fir",
        "green_enchanted",
        "holly",
        "imparius",
        "jacaranda",
        "lament",
        "mahogany",
        "maple",
        "nightshade",
        "palm",
        "pine",
        "rainbow_eucalyptus",
        "redwood",
        "skyris",
        "sythian",
        "white_mangrove",
        "willow",
        "witch_hazel",
        "zelkova",
    };

    private static final String[] STONE_VARIANTS = {
        "dacite",
        "dacite_brick",
        "dacite_cobblestone",
        "mossy_stone",
        "rocky_stone",
        "scoria_stone",
        "scoria_cobblestone",
        "scoria_stonebrick",
        "soapstone",
        "polished_soapstone",
        "soapstone_brick",
        "soapstone_tile",
        "red_rock",
        "red_rock_brick",
        "mossy_red_rock_brick",
        "cracked_red_rock_brick",
        "chiseled_red_rock_brick",
    };

    @Override
    public List<BlockVariant> getWoodVariants() {
        return Arrays.stream(WOOD_VARIANTS)
            .<BlockVariant>map(name -> new BlockVariant.Wood("byg/" + name))
            .toList();
    }

    @Override
    public List<BlockVariant> getStoneVariants() {
        return Arrays.stream(STONE_VARIANTS)
            .<BlockVariant>map(name -> new BlockVariant.Stone("byg/" + name))
            .toList();
    }
}

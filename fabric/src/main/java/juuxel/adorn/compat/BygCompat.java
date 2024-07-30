package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class BygCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
        return "byg";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
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
            "zelkova"
        );
    }

    @Override
    public List<BlockVariant> getStoneVariants() {
        return createVariants(
            BlockVariant.Stone::new,
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
            "chiseled_red_rock_brick"
        );
    }
}

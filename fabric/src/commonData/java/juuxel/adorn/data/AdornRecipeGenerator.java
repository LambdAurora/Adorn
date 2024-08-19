package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public final class AdornRecipeGenerator extends FabricRecipeProvider {
    public AdornRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        AdornBlocks.PAINTED_PLANKS.get().forEach((color, block) -> offerPlankDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_SLABS.get().forEach((color, block) -> offerPaintedSlabRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_SLABS.get().forEach((color, block) -> offerSlabDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_STAIRS.get().forEach((color, block) -> offerPaintedStairsRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_STAIRS.get().forEach((color, block) -> offerStairDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_FENCES.get().forEach((color, block) -> offerPaintedFenceRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_FENCES.get().forEach((color, block) -> offerFenceDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.get().forEach((color, block) -> offerPaintedFenceGateRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.get().forEach((color, block) -> offerFenceGateDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.get().forEach((color, block) -> offerPaintedPressurePlateRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.get().forEach((color, block) -> offerPressurePlateDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_BUTTONS.get().forEach((color, block) -> offerPaintedButtonRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_BUTTONS.get().forEach((color, block) -> offerButtonDyeingRecipe(exporter, block, color));
    }

    private static void offerPlankDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.PLANKS, "planks", false);
    }

    private static void offerPaintedSlabRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createSlabRecipe(RecipeCategory.BUILDING_BLOCKS, output, Ingredient.ofItems(planks))
            .group("wooden_slabs")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private static void offerSlabDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_SLABS, "slab", true);
    }

    private static void offerPaintedStairsRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createStairsRecipe(output, Ingredient.ofItems(planks))
            .group("wooden_stairs")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private static void offerStairDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_STAIRS, "stairs", true);
    }

    private static void offerPaintedFenceRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createFenceRecipe(output, Ingredient.ofItems(planks))
            .group("wooden_fence")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private static void offerFenceDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_FENCES, "fence", true);
    }

    private static void offerPaintedFenceGateRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createFenceGateRecipe(output, Ingredient.ofItems(planks))
            .group("wooden_fence")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private static void offerFenceGateDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.FENCE_GATES, "fence_gate", true);
    }

    private static void offerPaintedPressurePlateRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createPressurePlateRecipe(RecipeCategory.REDSTONE, output, Ingredient.ofItems(planks))
            .group("wooden_pressure_plate")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private static void offerPressurePlateDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_PRESSURE_PLATES, "pressure_plate", true);
    }

    private static void offerPaintedButtonRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createTransmutationRecipe(output, Ingredient.ofItems(planks))
            .group("wooden_button")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private static void offerButtonDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_BUTTONS, "button", true);
    }

    private static void offerDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color, TagKey<Item> ingredient, String kind, boolean suffix) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, output, 8)
            .input('*', TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/" + color.asString())))
            .input('#', ingredient)
            .pattern("###")
            .pattern("#*#")
            .pattern("###")
            .group("wooden_" + kind)
            .criterion("has_" + kind, conditionsFromTag(ingredient))
            .offerTo(exporter, suffix ? getItemPath(output) + "_from_dyeing" : getItemPath(output));
    }
}

package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
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
    }

    private static void offerPlankDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, output, 8)
            .input('*', TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/" + color.asString())))
            .input('#', ItemTags.PLANKS)
            .pattern("###")
            .pattern("#*#")
            .pattern("###")
            .group("planks")
            .criterion("has_planks", conditionsFromTag(ItemTags.PLANKS))
            .offerTo(exporter);
    }

    private static void offerPaintedSlabRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.get().get(color);
        createSlabRecipe(RecipeCategory.BUILDING_BLOCKS, output, Ingredient.ofItems(planks))
            .group("wooden_slabs")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private static void offerSlabDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, output, 8)
            .input('*', TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/" + color.asString())))
            .input('#', ItemTags.WOODEN_SLABS)
            .pattern("###")
            .pattern("#*#")
            .pattern("###")
            .group("wooden_slab")
            .criterion("has_slab", conditionsFromTag(ItemTags.WOODEN_SLABS))
            .offerTo(exporter, getItemPath(output) + "_from_dyeing");
    }
}

package juuxel.adorn.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;

public sealed interface BrewingRecipe extends Recipe<BrewerInventory> permits FluidBrewingRecipe, ItemBrewingRecipe {
    @Override
    default RecipeType<?> getType() {
        return AdornRecipes.BREWING_TYPE.get();
    }
}

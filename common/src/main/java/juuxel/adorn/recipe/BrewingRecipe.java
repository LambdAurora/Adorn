package juuxel.adorn.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;

public interface BrewingRecipe extends Recipe<BrewerInventory> {
    @Override
    default RecipeType<?> getType() {
        return AdornRecipes.BREWING_TYPE.get();
    }
}

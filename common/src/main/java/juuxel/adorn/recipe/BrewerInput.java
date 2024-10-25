package juuxel.adorn.recipe;

import juuxel.adorn.fluid.FluidReference;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.input.RecipeInput;

import java.util.Optional;

public interface BrewerInput extends RecipeInput {
    FluidReference getFluidReference();

    default boolean matches(int slot, Ingredient ingredient) {
        return ingredient.test(getStackInSlot(slot));
    }

    default boolean matches(int slot, Optional<Ingredient> ingredient) {
        return Ingredient.matches(ingredient, getStackInSlot(slot));
    }
}

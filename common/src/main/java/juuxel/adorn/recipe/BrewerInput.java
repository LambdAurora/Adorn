package juuxel.adorn.recipe;

import juuxel.adorn.fluid.FluidReference;
import net.minecraft.recipe.input.RecipeInput;

public interface BrewerInput extends RecipeInput {
    FluidReference getFluidReference();
}

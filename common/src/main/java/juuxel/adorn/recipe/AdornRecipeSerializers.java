package juuxel.adorn.recipe;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.RegistryKeys;

public final class AdornRecipeSerializers {
    public static final Registrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistrarFactory.get().create(RegistryKeys.RECIPE_SERIALIZER);

    public static final Registered<RecipeSerializer<ItemBrewingRecipe>> BREWING =
        RECIPE_SERIALIZERS.register("brewing", ItemBrewingRecipe.Serializer::new);
    public static final Registered<RecipeSerializer<FluidBrewingRecipe>> BREWING_FROM_FLUID =
        RECIPE_SERIALIZERS.register("brewing_from_fluid", FluidBrewingRecipe.Serializer::new);
    public static final Registered<RecipeSerializer<FertilizerRefillingRecipe>> FERTILIZER_REFILLING =
        RECIPE_SERIALIZERS.register("fertilizer_refilling", () -> new SpecialRecipeSerializer<>(FertilizerRefillingRecipe::new));

    public static void init() {
    }
}

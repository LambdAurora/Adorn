package juuxel.adorn.recipe;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.RegistryKeys;

public final class AdornRecipes {
    public static final Registrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistrarFactory.get().create(RegistryKeys.RECIPE_SERIALIZER);
    public static final Registrar<RecipeType<?>> RECIPE_TYPES = RegistrarFactory.get().create(RegistryKeys.RECIPE_TYPE);

    public static final Registered<RecipeType<BrewingRecipe>> BREWING_TYPE = registerRecipeType("brewing");
    public static final Registered<RecipeSerializer<ItemBrewingRecipe>> BREWING_SERIALIZER =
        RECIPE_SERIALIZERS.register("brewing", ItemBrewingRecipe.Serializer::new);
    public static final Registered<RecipeSerializer<FluidBrewingRecipe>> BREWING_FROM_FLUID_SERIALIZER =
        RECIPE_SERIALIZERS.register("brewing_from_fluid", FluidBrewingRecipe.Serializer::new);

    public static final Registered<RecipeSerializer<FertilizerRefillingRecipe>> FERTILIZER_REFILLING_SERIALIZER =
        RECIPE_SERIALIZERS.register("fertilizer_refilling", () -> new SpecialRecipeSerializer<>(FertilizerRefillingRecipe::new));

    private static <R extends Recipe<?>> Registered<RecipeType<R>> registerRecipeType(String id) {
        return RECIPE_TYPES.register(id, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return AdornCommon.NAMESPACE + ':' + id;
            }
        });
    }

    public static void init() {
    }
}

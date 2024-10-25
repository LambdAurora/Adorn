package juuxel.adorn.recipe;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryKeys;

public final class AdornRecipeBookCategories {
    public static final Registrar<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = RegistrarFactory.get().create(RegistryKeys.RECIPE_BOOK_CATEGORY);

    public static final Registered<RecipeBookCategory> BREWING = RECIPE_BOOK_CATEGORIES.register("brewing", RecipeBookCategory::new);

    public static void init() {
    }
}

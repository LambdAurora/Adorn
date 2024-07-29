package juuxel.adorn.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.client.gui.screen.TradingStationScreen;
import juuxel.adorn.recipe.AdornRecipes;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.ItemBrewingRecipe;
import juuxel.adorn.util.Logging;
import org.slf4j.Logger;

@EmiEntrypoint
public final class AdornEmiPlugin implements EmiPlugin {
    private static final Logger LOGGER = Logging.logger();

    public static final EmiRecipeCategory BREWER_CATEGORY = new EmiRecipeCategory(
        AdornCommon.id("brewer"),
        EmiStack.of(AdornBlocks.INSTANCE.getBREWER()),
        new EmiTexture(AdornCommon.id("textures/gui/recipe_viewer/brewer_light.png"), 240, 0, 16, 16)
    );

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(BREWER_CATEGORY);
        registry.addWorkstation(BREWER_CATEGORY, EmiStack.of(AdornBlocks.INSTANCE.getBREWER()));

        var recipeManager = registry.getRecipeManager();

        for (var entry : recipeManager.listAllOfType(AdornRecipes.BREWING_TYPE.get())) {
            BrewingEmiRecipe emiRecipe;
            // TODO: Pattern matching
            if (entry.value() instanceof ItemBrewingRecipe recipe) {
                emiRecipe = new BrewingEmiRecipe(entry.id(), recipe);
            } else if (entry.value() instanceof FluidBrewingRecipe recipe) {
                emiRecipe = new BrewingEmiRecipe(entry.id(), recipe);
            } else {
                LOGGER.error("Unknown brewing recipe: {}", entry.value());
                continue;
            }

            registry.addRecipe(emiRecipe);
        }

        registry.addDragDropHandler(TradingStationScreen.class, new TradingStationDragDropHandler());
    }
}

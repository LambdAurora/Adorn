package juuxel.adorn.compat.rei.client;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.compat.rei.AdornReiServer;
import juuxel.adorn.compat.rei.BrewerDisplay;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.recipe.AdornRecipes;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.ItemBrewingRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class AdornReiClient implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new BrewerCategory());
        registry.addWorkstations(AdornReiServer.BREWER, EntryStacks.of(AdornBlocks.BREWER.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(ItemBrewingRecipe.class, AdornRecipes.BREWING_TYPE.get(), entry -> new BrewerDisplay(entry.value()));
        registry.registerRecipeFiller(FluidBrewingRecipe.class, AdornRecipes.BREWING_TYPE.get(), entry -> new BrewerDisplay(entry.value()));
    }

    @Override
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        add(registry, AdornTags.SOFAS.item());
        add(registry, AdornTags.CHAIRS.item());
        add(registry, AdornTags.TABLES.item());
        add(registry, AdornTags.DRAWERS.item());
        add(registry, AdornTags.KITCHEN_COUNTERS.item());
        add(registry, AdornTags.KITCHEN_CUPBOARDS.item());
        add(registry, AdornTags.KITCHEN_SINKS.item());
        add(registry, AdornTags.POSTS.item());
        add(registry, AdornTags.PLATFORMS.item());
        add(registry, AdornTags.STEPS.item());
        add(registry, AdornTags.SHELVES.item());
        add(registry, AdornTags.CHIMNEYS.item());
        add(registry, AdornTags.COFFEE_TABLES.item());
        add(registry, AdornTags.BENCHES.item());
        add(registry, AdornTags.CRATES.item());
        add(registry, AdornTags.TABLE_LAMPS.item());
        add(registry, AdornTags.CANDLELIT_LANTERNS.item());
    }

    private static void add(CollapsibleEntryRegistry registry, TagKey<Item> tag) {
        // matches the translation keys used by EMI as well
        var name = Text.translatable(Util.createTranslationKey("tag", tag.id()));
        registry.group(tag.id(), name, EntryIngredients.ofItemTag(tag));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerDraggableStackVisitor(new TradingStationDraggableStackVisitor());
    }
}

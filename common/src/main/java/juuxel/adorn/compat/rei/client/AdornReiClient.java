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
        registry.addWorkstations(AdornReiServer.BREWER, EntryStacks.of(AdornBlocks.INSTANCE.getBREWER()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(ItemBrewingRecipe.class, AdornRecipes.BREWING_TYPE.get(), entry -> new BrewerDisplay(entry.value()));
        registry.registerRecipeFiller(FluidBrewingRecipe.class, AdornRecipes.BREWING_TYPE.get(), entry -> new BrewerDisplay(entry.value()));
    }

    @Override
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        add(registry, AdornTags.INSTANCE.getSOFAS().getItem());
        add(registry, AdornTags.INSTANCE.getCHAIRS().getItem());
        add(registry, AdornTags.INSTANCE.getTABLES().getItem());
        add(registry, AdornTags.INSTANCE.getDRAWERS().getItem());
        add(registry, AdornTags.INSTANCE.getKITCHEN_COUNTERS().getItem());
        add(registry, AdornTags.INSTANCE.getKITCHEN_CUPBOARDS().getItem());
        add(registry, AdornTags.INSTANCE.getKITCHEN_SINKS().getItem());
        add(registry, AdornTags.INSTANCE.getPOSTS().getItem());
        add(registry, AdornTags.INSTANCE.getPLATFORMS().getItem());
        add(registry, AdornTags.INSTANCE.getSTEPS().getItem());
        add(registry, AdornTags.INSTANCE.getSHELVES().getItem());
        add(registry, AdornTags.INSTANCE.getCHIMNEYS().getItem());
        add(registry, AdornTags.INSTANCE.getCOFFEE_TABLES().getItem());
        add(registry, AdornTags.INSTANCE.getBENCHES().getItem());
        add(registry, AdornTags.INSTANCE.getCRATES().getItem());
        add(registry, AdornTags.INSTANCE.getTABLE_LAMPS().getItem());
        add(registry, AdornTags.INSTANCE.getCANDLELIT_LANTERNS().getItem());
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

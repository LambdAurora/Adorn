package juuxel.adorn.platform.forge;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.criterion.AdornCriteria;
import juuxel.adorn.item.group.AdornItemGroups;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.loot.AdornLootConditionTypes;
import juuxel.adorn.loot.AdornLootFunctionTypes;
import juuxel.adorn.menu.AdornMenus;
import juuxel.adorn.platform.forge.client.AdornClient;
import juuxel.adorn.platform.forge.compat.Compat;
import juuxel.adorn.platform.forge.event.ItemEvents;
import juuxel.adorn.platform.forge.networking.AdornNetworking;
import juuxel.adorn.platform.forge.registrar.ForgeRegistrar;
import juuxel.adorn.recipe.AdornRecipes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForgeMod;

@Mod(AdornCommon.NAMESPACE)
public final class Adorn {
    public Adorn() {
        var modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        ConfigManager.get().init();
        modBus.addListener(this::init);
        new EventsImplementedInJava().register(modBus);
        AdornItemGroups.init();
        registerToBus(AdornItemGroups.ITEM_GROUPS, modBus);
        AdornRecipes.init();
        registerToBus(AdornMenus.MENUS, modBus);
        registerToBus(AdornRecipes.RECIPE_SERIALIZERS, modBus);
        registerToBus(AdornRecipes.RECIPE_TYPES, modBus);
        registerToBus(AdornLootConditionTypes.LOOT_CONDITION_TYPES, modBus);
        registerToBus(AdornLootFunctionTypes.LOOT_FUNCTION_TYPES, modBus);
        modBus.addListener(AdornNetworking::register);
        AdornCriteria.init();
        registerToBus(AdornCriteria.CRITERIA, modBus);
        ItemEvents.register(modBus);
        modBus.addListener(AdornCapabilities::register);
        Compat.init(modBus);
        BlockVariantSets.register();
        NeoForgeMod.enableMilkFluid();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            AdornClient.init(modBus);
        }
    }

    private void registerToBus(Registrar<?> registrar, IEventBus modBus) {
        ((ForgeRegistrar<?>) registrar).hook(modBus);
    }

    private void init(FMLCommonSetupEvent event) {
        AdornStats.init();
        ConfigManager.get().finalize();
    }
}

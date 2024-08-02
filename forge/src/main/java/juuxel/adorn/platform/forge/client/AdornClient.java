package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.gui.TradeTooltipComponent;
import juuxel.adorn.client.gui.screen.AdornMenuScreens;
import juuxel.adorn.client.gui.screen.GuideBookScreen;
import juuxel.adorn.client.gui.screen.MainConfigScreen;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.trading.Trade;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

public final class AdornClient {
    public static void init(IEventBus modBus) {
        modBus.addListener(AdornClient::setup);
        modBus.addListener(AdornRenderers::registerRenderers);
        modBus.addListener(AdornClient::registerTooltipComponent);
        var resourceManager = (ReloadableResourceManagerImpl) MinecraftClient.getInstance().getResourceManager();
        resourceManager.registerReloader(PlatformBridges.get().getResources().getBookManager());
        resourceManager.registerReloader(PlatformBridges.get().getResources().getColorManager());
        ModLoadingContext.get().registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> new MainConfigScreen(parent))
        );
    }

    private static void setup(FMLClientSetupEvent event) {
        AdornMenuScreens.register();
    }

    private static void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(Trade.class, TradeTooltipComponent::new);
    }

    public static void openBookScreen(Identifier bookId) {
        MinecraftClient.getInstance().setScreen(new GuideBookScreen(PlatformBridges.get().getResources().getBookManager().get(bookId)));
    }
}

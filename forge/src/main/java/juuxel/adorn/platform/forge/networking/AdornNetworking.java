package juuxel.adorn.platform.forge.networking;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.client.gui.screen.BrewerScreen;
import juuxel.adorn.menu.TradingStationMenu;
import juuxel.adorn.platform.forge.client.AdornClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public final class AdornNetworking {
    public static final Identifier OPEN_BOOK = AdornCommon.id("open_book");
    public static final Identifier BREWER_FLUID_SYNC = AdornCommon.id("brewer_fluid_sync");
    public static final Identifier SET_TRADE_STACK = AdornCommon.id("set_trade_stack");

    public static void register(RegisterPayloadHandlerEvent event) {
         var registrar = event.registrar(AdornCommon.NAMESPACE);
         registrar.play(OPEN_BOOK, OpenBookS2CMessage::fromPacket, builder -> builder.client(AdornNetworking::handleBookOpen));
         registrar.play(BREWER_FLUID_SYNC, BrewerFluidSyncS2CMessage::fromPacket, builder -> builder.client(AdornNetworking::handleBrewerFluidSync));
         registrar.play(SET_TRADE_STACK, SetTradeStackC2SMessage::fromPacket, builder -> builder.server(AdornNetworking::handleSetTradeStack));
    }

    private static void handleBookOpen(OpenBookS2CMessage message, PlayPayloadContext context) {
        context.workHandler().execute(() -> AdornClient.openBookScreen(message.bookId()));
    }

    private static void handleBrewerFluidSync(BrewerFluidSyncS2CMessage message, PlayPayloadContext context) {
        context.workHandler().execute(() -> BrewerScreen.setFluidFromPacket(MinecraftClient.getInstance(), message.syncId(), message.fluid()));
    }

    private static void handleSetTradeStack(SetTradeStackC2SMessage message, PlayPayloadContext context) {
        context.workHandler().execute(() -> {
            var sender = context.player().orElseThrow();
            var menu = sender.menu;

            if (menu.syncId == message.syncId() && menu instanceof TradingStationMenu tradingStationMenu) {
                tradingStationMenu.updateTradeStack(message.slotId(), message.stack(), sender);
            }
        });
    }
}

package juuxel.adorn.lib;

import juuxel.adorn.client.gui.screen.BrewerScreen;
import juuxel.adorn.client.gui.screen.GuideBookScreen;
import juuxel.adorn.client.resources.BookManagerFabric;
import juuxel.adorn.menu.TradingStationMenu;
import juuxel.adorn.networking.BrewerFluidSyncS2CMessage;
import juuxel.adorn.networking.OpenBookS2CMessage;
import juuxel.adorn.networking.SetTradeStackC2SMessage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class AdornNetworking {
    public static void init() {
        PayloadTypeRegistry.playC2S().register(SetTradeStackC2SMessage.ID, SetTradeStackC2SMessage.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(BrewerFluidSyncS2CMessage.ID, BrewerFluidSyncS2CMessage.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(OpenBookS2CMessage.ID, OpenBookS2CMessage.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SetTradeStackC2SMessage.ID, (payload, context) -> {
            var menu = context.player().menu;
            if (menu.syncId == payload.syncId() && menu instanceof TradingStationMenu tradingStationMenu) {
                tradingStationMenu.updateTradeStack(payload.slotId(), payload.stack(), context.player());
            }
        });
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(OpenBookS2CMessage.ID, (payload, context) -> {
            context.client().setScreen(new GuideBookScreen(BookManagerFabric.INSTANCE.get(payload.bookId())));
        });

        ClientPlayNetworking.registerGlobalReceiver(BrewerFluidSyncS2CMessage.ID, (payload, context) -> {
            BrewerScreen.setFluidFromPacket(context.client(), payload.syncId(), payload.fluid());
        });
    }
}

package juuxel.adorn.lib;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.client.gui.screen.BrewerScreen;
import juuxel.adorn.client.gui.screen.GuideBookScreen;
import juuxel.adorn.client.resources.BookManagerFabric;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidVolume;
import juuxel.adorn.menu.TradingStationMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class AdornNetworking {
    public static final Identifier OPEN_BOOK = AdornCommon.id("open_book");
    public static final Identifier BREWER_FLUID_SYNC = AdornCommon.id("brewer_fluid_sync");
    public static final Identifier SET_TRADE_STACK = AdornCommon.id("set_trade_stack");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(SET_TRADE_STACK, (server, player, handler, buf, responseSender) -> {
            var syncId = buf.readVarInt();
            var slotId = buf.readVarInt();
            var stack = buf.readItemStack();
            server.execute(() -> {
                var menu = player.menu;
                if (menu.syncId == syncId && menu instanceof TradingStationMenu tradingStationMenu) {
                    tradingStationMenu.updateTradeStack(slotId, stack, player);
                }
            });
        });
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(OPEN_BOOK, (client, handler, buf, responseSender) -> {
            var bookId = buf.readIdentifier();
            client.execute(() -> client.setScreen(new GuideBookScreen(BookManagerFabric.INSTANCE.get(bookId))));
        });

        ClientPlayNetworking.registerGlobalReceiver(BREWER_FLUID_SYNC, (client, handler, buf, responseSender) -> {
            int syncId = buf.readUnsignedByte();
            var volume = FluidVolume.load(buf);
            client.execute(() -> BrewerScreen.setFluidFromPacket(client, syncId, volume));
        });
    }

    public static void sendOpenBookPacket(PlayerEntity player, Identifier bookId) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            var buf = PacketByteBufs.create();
            buf.writeIdentifier(bookId);
            ServerPlayNetworking.send(serverPlayer, OPEN_BOOK, buf);
        }
    }

    public static void sendBrewerFluidSync(PlayerEntity player, int syncId, FluidReference fluid) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            var buf = PacketByteBufs.create();
            buf.writeByte(syncId);
            fluid.write(buf);
            ServerPlayNetworking.send(serverPlayer, BREWER_FLUID_SYNC, buf);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void sendSetTradeStack(int syncId, int slotId, ItemStack stack) {
        var buf = PacketByteBufs.create();
        buf.writeVarInt(syncId);
        buf.writeVarInt(slotId);
        buf.writeItemStack(stack);
        ClientPlayNetworking.send(SET_TRADE_STACK, buf);
    }
}

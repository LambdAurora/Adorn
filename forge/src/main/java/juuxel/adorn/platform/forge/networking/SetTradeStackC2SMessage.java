package juuxel.adorn.platform.forge.networking;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SetTradeStackC2SMessage(int syncId, int slotId, ItemStack stack) implements CustomPayload {
    @Override
    public Identifier id() {
        return AdornNetworking.SET_TRADE_STACK;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(syncId);
        buf.writeVarInt(slotId);
        buf.writeItemStack(stack);
    }

    public static SetTradeStackC2SMessage fromPacket(PacketByteBuf buf) {
        return new SetTradeStackC2SMessage(
            buf.readVarInt(),
            buf.readVarInt(),
            buf.readItemStack()
        );
    }
}

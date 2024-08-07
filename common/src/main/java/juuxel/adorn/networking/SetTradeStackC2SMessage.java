package juuxel.adorn.networking;

import juuxel.adorn.AdornCommon;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SetTradeStackC2SMessage(int syncId, int slotId, ItemStack stack) implements CustomPayload {
    public static final Id<SetTradeStackC2SMessage> ID = new Id<>(AdornCommon.id("set_trade_stack"));
    public static final PacketCodec<RegistryByteBuf, SetTradeStackC2SMessage> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.VAR_INT,
        SetTradeStackC2SMessage::syncId,
        PacketCodecs.VAR_INT,
        SetTradeStackC2SMessage::slotId,
        ItemStack.PACKET_CODEC,
        SetTradeStackC2SMessage::stack,
        SetTradeStackC2SMessage::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

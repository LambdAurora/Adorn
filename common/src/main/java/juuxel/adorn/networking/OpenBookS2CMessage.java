package juuxel.adorn.networking;

import juuxel.adorn.AdornCommon;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenBookS2CMessage(Identifier bookId) implements CustomPayload {
    public static final Id<OpenBookS2CMessage> ID = new Id<>(AdornCommon.id("open_book"));
    public static final PacketCodec<PacketByteBuf, OpenBookS2CMessage> PACKET_CODEC =
        Identifier.PACKET_CODEC.xmap(OpenBookS2CMessage::new, OpenBookS2CMessage::bookId).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

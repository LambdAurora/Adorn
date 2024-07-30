package juuxel.adorn.platform.forge.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenBookS2CMessage(Identifier bookId) implements CustomPayload {
    @Override
    public Identifier id() {
        return AdornNetworking.OPEN_BOOK;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(bookId);
    }

    public static OpenBookS2CMessage fromPacket(PacketByteBuf buf) {
        return new OpenBookS2CMessage(buf.readIdentifier());
    }
}

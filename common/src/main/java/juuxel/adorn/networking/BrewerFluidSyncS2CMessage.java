package juuxel.adorn.networking;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.fluid.FluidVolume;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record BrewerFluidSyncS2CMessage(int syncId, FluidVolume fluid) implements CustomPayload {
    public static final Id<BrewerFluidSyncS2CMessage> ID = new Id<>(AdornCommon.id("brewer_fluid_sync"));
    public static final PacketCodec<RegistryByteBuf, BrewerFluidSyncS2CMessage> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.VAR_INT,
        BrewerFluidSyncS2CMessage::syncId,
        FluidVolume.PACKET_CODEC,
        BrewerFluidSyncS2CMessage::fluid,
        BrewerFluidSyncS2CMessage::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

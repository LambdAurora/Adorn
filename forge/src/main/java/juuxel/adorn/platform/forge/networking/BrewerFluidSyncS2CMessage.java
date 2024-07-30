package juuxel.adorn.platform.forge.networking;

import juuxel.adorn.fluid.FluidVolume;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record BrewerFluidSyncS2CMessage(int syncId, FluidVolume fluid) implements CustomPayload {
    @Override
    public Identifier id() {
        return AdornNetworking.BREWER_FLUID_SYNC;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByte(syncId);
        fluid.write(buf);
    }

    public static BrewerFluidSyncS2CMessage fromPacket(PacketByteBuf buf) {
        return new BrewerFluidSyncS2CMessage(
            buf.readUnsignedByte(),
            FluidVolume.load(buf)
        );
    }
}

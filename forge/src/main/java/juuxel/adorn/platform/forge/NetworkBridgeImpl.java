package juuxel.adorn.platform.forge;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.platform.NetworkBridge;
import juuxel.adorn.platform.forge.networking.BrewerFluidSyncS2CMessage;
import juuxel.adorn.platform.forge.networking.OpenBookS2CMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;

public final class NetworkBridgeImpl implements NetworkBridge {
    public static final NetworkBridgeImpl INSTANCE = new NetworkBridgeImpl();

    @Override
    public void sendToTracking(Entity entity, Packet<?> packet) {
        PacketDistributor.TRACKING_ENTITY.with(entity).send(packet);
    }

    @Override
    public void sendOpenBookPacket(PlayerEntity player, Identifier bookId) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            PacketDistributor.PLAYER.with(serverPlayer).send(new OpenBookS2CMessage(bookId));
        }
    }

    @Override
    public void sendBrewerFluidSync(PlayerEntity player, int syncId, FluidReference fluid) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            PacketDistributor.PLAYER.with(serverPlayer).send(new BrewerFluidSyncS2CMessage(syncId, fluid.createSnapshot()));
        }
    }
}

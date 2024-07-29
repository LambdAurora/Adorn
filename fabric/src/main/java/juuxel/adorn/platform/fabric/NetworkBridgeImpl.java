package juuxel.adorn.platform.fabric;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.lib.AdornNetworking;
import juuxel.adorn.platform.NetworkBridge;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;

public final class NetworkBridgeImpl implements NetworkBridge {
    public static final NetworkBridgeImpl INSTANCE = new NetworkBridgeImpl();

    @Override
    public void sendToTracking(Entity entity, Packet<?> packet) {
        for (var player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.getSender(player).sendPacket(packet);
        }
    }

    @Override
    public void sendOpenBookPacket(PlayerEntity player, Identifier bookId) {
        AdornNetworking.sendOpenBookPacket(player, bookId);
    }

    @Override
    public void sendBrewerFluidSync(PlayerEntity player, int syncId, FluidReference fluid) {
        AdornNetworking.sendBrewerFluidSync(player, syncId, fluid);
    }
}

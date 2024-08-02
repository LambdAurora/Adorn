package juuxel.adorn.platform;

import juuxel.adorn.fluid.FluidReference;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public interface NetworkBridge {
    void sendToTracking(Entity entity, Packet<?> packet);
    void sendOpenBookPacket(PlayerEntity player, Identifier bookId);
    void sendBrewerFluidSync(PlayerEntity player, int syncId, FluidReference fluid);

    default void syncBlockEntity(BlockEntity be) {
        if (!(be.getWorld() instanceof ServerWorld world)) {
            throw new IllegalStateException("[Adorn] Block entities cannot be synced client->server");
        }
        world.getChunkManager().markForUpdate(be.getPos());
    }
}

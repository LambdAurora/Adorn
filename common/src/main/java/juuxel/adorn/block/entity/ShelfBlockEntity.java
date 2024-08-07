package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.menu.Menu;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public final class ShelfBlockEntity extends BaseContainerBlockEntity {
    public ShelfBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.SHELF.get(), pos, state, 2);
    }

    @Override
    protected Menu createMenu(int syncId, PlayerInventory inv) {
        // No menus for shelves
        return null;
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createComponentlessNbt(registries);
    }
}

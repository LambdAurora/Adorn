package juuxel.adorn.platform;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.menu.Menu;
import net.minecraft.menu.MenuType;
import net.minecraft.menu.NamedMenuFactory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface MenuBridge {
    /**
     * Opens a menu with a pos with the opening NBT sent to the client.
     * Does nothing on the client.
     */
    void open(PlayerEntity player, @Nullable NamedMenuFactory factory, BlockPos pos);

    <M extends Menu> MenuType<M> createType(Factory<M> factory);

    @FunctionalInterface
    interface Factory<M extends Menu> {
        M create(int syncId, PlayerInventory inventory, PacketByteBuf buf);
    }
}

package juuxel.adorn.menu;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.menu.MenuContext;
import net.minecraft.network.PacketByteBuf;

public final class DrawerMenu extends SimpleMenu {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 3;

    public DrawerMenu(int syncId, PlayerInventory playerInventory, Inventory inventory, MenuContext context) {
        super(AdornMenus.DRAWER.get(), syncId, WIDTH, HEIGHT, inventory, playerInventory, context);
    }

    public static DrawerMenu load(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        var pos = buf.readBlockPos();
        var context = MenuContext.create(playerInventory.player.getWorld(), pos);
        return new DrawerMenu(syncId, playerInventory, new SimpleInventory(WIDTH * HEIGHT), context);
    }
}

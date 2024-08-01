package juuxel.adorn.menu;

import net.minecraft.inventory.Inventory;
import net.minecraft.menu.MenuContext;

public interface ContainerBlockMenu {
    Inventory getInventory();
    MenuContext getContext();
}

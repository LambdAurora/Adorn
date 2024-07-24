package juuxel.adorn.recipe;

import juuxel.adorn.fluid.FluidReference;
import net.minecraft.inventory.Inventory;

public interface BrewerInventory extends Inventory {
    FluidReference getFluidReference();
}

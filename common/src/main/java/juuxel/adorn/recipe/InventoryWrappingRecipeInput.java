package juuxel.adorn.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public class InventoryWrappingRecipeInput<I extends Inventory> implements RecipeInput {
    protected final I parent;

    public InventoryWrappingRecipeInput(I parent) {
        this.parent = parent;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return parent.getStack(slot);
    }

    @Override
    public int getSize() {
        return parent.size();
    }

    @Override
    public boolean isEmpty() {
        return parent.isEmpty();
    }
}

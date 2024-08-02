package juuxel.adorn.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryComponent implements Inventory, NbtConvertible {
    private final int size;
    private final List<InventoryChangedListener> listeners = new ArrayList<>();
    private final DefaultedList<ItemStack> items;

    public InventoryComponent(int size) {
        this.size = size;
        items = DefaultedList.ofSize(size, ItemStack.EMPTY);
    }

    private InventoryComponent(DefaultedList<ItemStack> items) {
        this(items.size());
        for (int i = 0; i < items.size(); i++) {
            this.items.set(i, items.get(i));
        }
    }

    /**
     * Creates a copy of this inventory, not retaining any listeners.
     */
    public InventoryComponent copy() {
        return new InventoryComponent(items);
    }

    /**
     * Checks if the stack can be extracted from this inventory. Ignores NBT, durability and tags.
     */
    public boolean canExtract(ItemStack stack) {
        int remainingAmount = stack.getCount();

        for (var invStack : items) {
            if (ItemStack.areItemsEqual(invStack, stack)) {
                remainingAmount -= invStack.getCount();
                if (remainingAmount <= 0) return true;
            }
        }

        return false;
    }

    /**
     * Tries to remove the stack from this inventory. Ignores tags.
     *
     * @return {@code true} if extracted
     */
    public boolean tryExtract(ItemStack stack) {
        int remainingAmount = stack.getCount();

        for (var invStack : items) {
            if (ItemStack.areItemsEqual(invStack, stack) && Objects.equals(invStack.getNbt(), stack.getNbt())) {
                int invStackAmount = invStack.getCount();
                invStack.decrement(Math.min(invStackAmount, remainingAmount));
                remainingAmount -= invStackAmount;
                if (remainingAmount <= 0) return true;
            }
        }

        return false;
    }

    /**
     * Checks if the stack can be inserted to this inventory. Ignores tags.
     */
    public boolean canInsert(ItemStack stack) {
        int remainingAmount = stack.getCount();

        for (var invStack : items) {
            if (ItemStack.areItemsEqual(invStack, stack) && invStack.getCount() < invStack.getMaxCount() && Objects.equals(stack.getNbt(), invStack.getNbt())) {
                int insertionAmount = Math.min(invStack.getMaxCount() - invStack.getCount(), remainingAmount);
                remainingAmount -= insertionAmount;
                if (remainingAmount <= 0) return true;
            } else if (invStack.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tries to insert the stack to this inventory. Ignores tags.
     *
     * @return {@code true} if inserted
     */
    public boolean tryInsert(ItemStack stack) {
        int remainingAmount = stack.getCount();

        for (int slot = 0; slot < items.size(); slot++) {
            var invStack = items.get(slot);
            if (ItemStack.areItemsEqual(invStack, stack) && invStack.getCount() < invStack.getMaxCount() && Objects.equals(invStack.getNbt(), stack.getNbt())) {
                int insertionAmount = Math.min(invStack.getMaxCount() - invStack.getCount(), remainingAmount);
                remainingAmount -= insertionAmount;
                invStack.increment(insertionAmount);
                if (remainingAmount <= 0) return true;
            } else if (invStack.isEmpty()) {
                items.set(slot, stack.copy());
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the count of items with the same item and NBT as the stack.
     * Ignores the stack's count.
     */
    public int getAmountWithNbt(ItemStack stack) {
        return CollectionUtil.sumOf(items, it -> stack.getItem() == it.getItem() && Objects.equals(stack.getNbt(), it.getNbt()) ? it.getCount() : 0);
    }

    // -----
    // NBT
    // -----

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, items);
    }

    // -------------------------------
    // Inventory management/transfer
    // -------------------------------

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public void clear() {
        items.clear();
        markDirty();
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(items, slot);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        var stack = Inventories.splitStack(items, slot, count);
        if (!stack.isEmpty()) markDirty();
        return stack;
    }

    @Override
    public boolean isEmpty() {
        for (var stack : items) {
            if (!stack.isEmpty()) return false;
        }

        return true;
    }

    // -----------
    // Listeners
    // -----------

    @Override
    public void markDirty() {
        for (var listener : listeners) {
            listener.onInventoryChanged(this);
        }
    }

    public void addListener(InventoryChangedListener listener) {
        listeners.add(listener);
    }
}

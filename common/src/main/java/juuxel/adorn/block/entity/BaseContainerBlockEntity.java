package juuxel.adorn.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

/**
 * A container block entity that might not have a menu.
 * This class handles the serialisation and the container logic.
 */
public abstract class BaseContainerBlockEntity extends LootableContainerBlockEntity {
    private final int size;
    private DefaultedList<ItemStack> items;

    public BaseContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state);
        items = DefaultedList.ofSize(size, ItemStack.EMPTY);
        this.size = size;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!writeLootTable(nbt)) {
            Inventories.writeNbt(nbt, items);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        items = DefaultedList.ofSize(size, ItemStack.EMPTY);
        if (!readLootTable(nbt)) {
            Inventories.readNbt(nbt, items);
        }
    }

    @Override
    protected DefaultedList<ItemStack> method_11282() {
        return items;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        items = list;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected Text getContainerName() {
        return getCachedState().getBlock().getName();
    }
}

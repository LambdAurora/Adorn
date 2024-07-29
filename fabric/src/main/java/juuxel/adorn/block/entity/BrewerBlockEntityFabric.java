package juuxel.adorn.block.entity;

import com.google.common.base.Predicates;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.util.FluidStorageReference;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public final class BrewerBlockEntityFabric extends BrewerBlockEntity {
    private static final String NBT_FLUID = "Fluid";
    private static final String NBT_VOLUME = "Volume";

    private final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected long getCapacity(FluidVariant variant) {
            return FLUID_CAPACITY_IN_BUCKETS * FluidConstants.BUCKET;
        }

        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };

    private final FluidReference fluidReference = new FluidStorageReference(fluidStorage);

    public BrewerBlockEntityFabric(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public SingleVariantStorage<FluidVariant> getFluidStorage() {
        return fluidStorage;
    }

    @Override
    public FluidReference getFluidReference() {
        return fluidReference;
    }

    @Override
    protected boolean canExtractFluidContainer() {
        try (var transaction = Transaction.openOuter()) {
            return extractFluidContainer(transaction) == 0L;
        }
    }

    @Override
    protected void tryExtractFluidContainer() {
        extractFluidContainer(null);
    }

    private long extractFluidContainer(@Nullable TransactionContext transaction) {
        var fluidContainerSlot = InventoryStorage.of(this, null).getSlot(FLUID_CONTAINER_SLOT);
        var itemStorage = FluidStorage.ITEM.find(getStack(FLUID_CONTAINER_SLOT), ContainerItemContext.ofSingleSlot(fluidContainerSlot));
        return StorageUtil.move(itemStorage, fluidStorage, Predicates.alwaysTrue(), Long.MAX_VALUE, transaction);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put(NBT_FLUID, fluidStorage.variant.toNbt());
        nbt.putLong(NBT_VOLUME, fluidStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        fluidStorage.variant = FluidVariant.fromNbt(nbt.getCompound(NBT_FLUID));
        fluidStorage.amount = nbt.getLong(NBT_VOLUME);
    }
}

package juuxel.adorn.platform.forge;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.platform.forge.block.entity.BlockEntityWithFluidTank;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.math.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

public final class AdornCapabilities {
    private static final IBlockCapabilityProvider<IItemHandler, @Nullable Direction> INVENTORY_WRAPPER_FOR_BLOCK =
        (world, pos, state, blockEntity, side) -> blockEntity instanceof Inventory inventory ? getInventoryWrapper(inventory, side) : null;
    private static final ICapabilityProvider<BlockEntity, @Nullable Direction, IItemHandler> INVENTORY_WRAPPER_FOR_BLOCK_ENTITY =
        (blockEntity, side) -> blockEntity instanceof Inventory inventory ? getInventoryWrapper(inventory, side) : null;
    private static final IBlockCapabilityProvider<IFluidHandler, @Nullable Direction> FLUID_TANK_FOR_BLOCK =
        (world, pos, state, blockEntity, side) -> blockEntity instanceof BlockEntityWithFluidTank withTank ? withTank.getTank() : null;
    private static final ICapabilityProvider<BlockEntity, @Nullable Direction, IFluidHandler> FLUID_TANK_FOR_BLOCK_ENTITY =
        (blockEntity, side) -> blockEntity instanceof BlockEntityWithFluidTank withTank ? withTank.getTank() : null;

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AdornBlockEntities.BREWER.get(), INVENTORY_WRAPPER_FOR_BLOCK_ENTITY);

        var containerBlockKinds = new BlockKind[] {
            BlockKind.DRAWER,
            BlockKind.KITCHEN_CUPBOARD,
            BlockKind.SHELF,
        };

        for (var kind : containerBlockKinds) {
            for (var block : BlockVariantSets.get(kind)) {
                event.registerBlock(Capabilities.ItemHandler.BLOCK, INVENTORY_WRAPPER_FOR_BLOCK, block.get());
            }
        }

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AdornBlockEntities.BREWER.get(), FLUID_TANK_FOR_BLOCK_ENTITY);

        for (var kitchenSink : BlockVariantSets.get(BlockKind.KITCHEN_SINK)) {
            event.registerBlock(Capabilities.FluidHandler.BLOCK, FLUID_TANK_FOR_BLOCK, kitchenSink.get());
        }
    }

    private static IItemHandler getInventoryWrapper(Inventory inventory, @Nullable Direction side) {
        return side != null && inventory instanceof SidedInventory sided ? new SidedInvWrapper(sided, side) : new InvWrapper(inventory);
    }
}

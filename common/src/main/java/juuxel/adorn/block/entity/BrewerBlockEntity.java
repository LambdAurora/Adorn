package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.BrewerBlock;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.menu.BrewerMenu;
import juuxel.adorn.recipe.AdornRecipes;
import juuxel.adorn.recipe.BrewerInventory;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.menu.Menu;
import net.minecraft.menu.property.PropertyDelegate;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BrewerBlockEntity extends BaseContainerBlockEntity implements SidedInventory, BrewerInventory {
    private static final String NBT_PROGRESS = "Progress";
    public static final int CONTAINER_SIZE = 4;
    public static final int INPUT_SLOT = 0;
    public static final int LEFT_INGREDIENT_SLOT = 1;
    public static final int RIGHT_INGREDIENT_SLOT = 2;
    public static final int FLUID_CONTAINER_SLOT = 3;
    public static final int MAX_PROGRESS = 200;
    public static final int FLUID_CAPACITY_IN_BUCKETS = 2;

    private int progress = 0;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                default -> throw new IllegalArgumentException("Unknown property: " + index);
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                default -> throw new IllegalArgumentException("Unknown property: " + index);
            }
        }

        @Override
        public int size() {
            return 1;
        }
    };

    public BrewerBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.BREWER.get(), pos, state, CONTAINER_SIZE);
    }

    @Override
    protected Menu createMenu(int syncId, PlayerInventory inv) {
        return new BrewerMenu(syncId, inv, this, propertyDelegate, getFluidReference());
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt(NBT_PROGRESS, progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt(NBT_PROGRESS);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        var facing = getCachedState().get(BrewerBlock.FACING);

        if (side == facing.rotateYClockwise()) {
            return new int[] { LEFT_INGREDIENT_SLOT };
        } else if (side == facing.rotateYCounterclockwise()) {
            return new int[] { RIGHT_INGREDIENT_SLOT };
        } else if (side == facing.getOpposite()) {
            return new int[] { FLUID_CONTAINER_SLOT };
        } else if (side == Direction.UP) {
            return new int[] { INPUT_SLOT };
        } else if (side == Direction.DOWN) {
            return new int[] { INPUT_SLOT, FLUID_CONTAINER_SLOT };
        } else {
            return new int[0];
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot == INPUT_SLOT && !(stack.isOf(AdornItems.MUG.get()) && getStack(slot).isEmpty())) return false;
        if (slot == FLUID_CONTAINER_SLOT && !getStack(slot).isEmpty()) return false;
        return true;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return dir != Direction.DOWN && isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && (slot != FLUID_CONTAINER_SLOT || canExtractFluidContainer());
    }

    public int calculateComparatorOutput() {
        // If brewing has finished
        var mugStack = getStack(INPUT_SLOT);
        if (!mugStack.isEmpty() && !mugStack.isOf(AdornItems.MUG.get())) {
            return 15;
        }

        var progressFraction = (float) progress / MAX_PROGRESS;
        var level = progressFraction * 14;
        return MathHelper.ceil(level);
    }

    /** {@return true if you can extract the fluid container as an item} */
    protected abstract boolean canExtractFluidContainer();

    /** Extract the fluid from the container, if possible. */
    protected abstract void tryExtractFluidContainer();

    private boolean isActive() {
        return progress != 0;
    }

    private static void decrementIngredient(BrewerBlockEntity brewer, int slot) {
        var stack = brewer.getStack(slot);
        // TODO: Use stack-aware version on Fabric (and Neo if available)
        var remainder = stack.getItem().getRecipeRemainder();
        stack.decrement(1);

        if (remainder != null) {
            if (stack.isEmpty()) {
                brewer.setStack(slot, new ItemStack(remainder));
            } else {
                ItemScatterer.spawn(brewer.world, brewer.pos.getX() + 0.5, brewer.pos.getY() + 0.5, brewer.pos.getZ() + 0.5, new ItemStack(remainder));
            }
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, BrewerBlockEntity brewer) {
        var originallyActive = brewer.isActive();
        brewer.tryExtractFluidContainer();

        var dirty = false;
        var hasMug = !brewer.getStack(INPUT_SLOT).isEmpty();

        if (hasMug != state.get(BrewerBlock.HAS_MUG)) {
            world.setBlockState(pos, state.with(BrewerBlock.HAS_MUG, hasMug));
        }

        var recipe = world.getRecipeManager().getFirstMatch(AdornRecipes.BREWING_TYPE.get(), brewer, world).map(RecipeEntry::value).orElse(null);

        if (recipe != null && brewer.getStack(INPUT_SLOT).isOf(AdornItems.MUG.get())) {
            if (brewer.progress++ >= MAX_PROGRESS) {
                decrementIngredient(brewer, LEFT_INGREDIENT_SLOT);
                decrementIngredient(brewer, RIGHT_INGREDIENT_SLOT);
                brewer.setStack(INPUT_SLOT, recipe.craft(brewer, world.getRegistryManager()));

                if (recipe instanceof FluidBrewingRecipe fluidRecipe) {
                    brewer.getFluidReference().decrement(fluidRecipe.fluid().amount(), fluidRecipe.fluid().unit());
                }
            }

            dirty = true;
        } else {
            if (brewer.progress != 0) {
                brewer.progress = 0;
                dirty = true;
            }
        }

        var activeNow = brewer.isActive();
        if (originallyActive != activeNow) {
            dirty = true;
            var newState = state.with(BrewerBlock.ACTIVE, activeNow);
            world.setBlockState(pos, newState);
        }

        if (dirty) {
            markDirty(world, pos, state);
        }
    }
}

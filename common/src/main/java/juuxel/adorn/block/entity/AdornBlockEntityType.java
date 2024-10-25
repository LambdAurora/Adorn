package juuxel.adorn.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import java.util.Set;
import java.util.function.Predicate;

public final class AdornBlockEntityType<E extends BlockEntity> extends BlockEntityType<E> {
    private final Predicate<Block> blockPredicate;

    public AdornBlockEntityType(BlockEntityFactory<? extends E> factory, Predicate<Block> blockPredicate) {
        super(factory, Set.of());
        this.blockPredicate = blockPredicate;
    }

    @Override
    public boolean supports(BlockState state) {
        return blockPredicate.test(state.getBlock());
    }
}

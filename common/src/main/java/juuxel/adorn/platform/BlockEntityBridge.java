package juuxel.adorn.platform;

import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public interface BlockEntityBridge {
    BrewerBlockEntity createBrewer(BlockPos pos, BlockState state);
    KitchenSinkBlockEntity createKitchenSink(BlockPos pos, BlockState state);
}

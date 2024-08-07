package juuxel.adorn.commonmixin;

import com.llamalad7.mixinextras.sugar.Local;
import juuxel.adorn.block.PicketFenceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
abstract class BlockMixin {
    @Inject(method = "sideCoversSmallSquare", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/WorldView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), cancellable = true)
    private static void adorn_onSideCoversSmallSquare(WorldView world, BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> info, @Local BlockState state) {
        Block block = state.getBlock();
        if (block instanceof PicketFenceBlock picketFence && !picketFence.sideCoversSmallSquare(state)) {
            info.setReturnValue(false);
        }
    }
}

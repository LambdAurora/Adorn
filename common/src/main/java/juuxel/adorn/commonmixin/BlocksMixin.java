package juuxel.adorn.commonmixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import juuxel.adorn.block.AdornBlockSetTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Blocks.class)
abstract class BlocksMixin {
    @WrapOperation(method = "createWoodenButtonBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;create()Lnet/minecraft/block/AbstractBlock$Settings;"))
    private static AbstractBlock.Settings addMapColorToButton(Operation<AbstractBlock.Settings> operation, BlockSetType blockSetType) {
        var settings = operation.call();
        var color = AdornBlockSetTypes.PAINTED_WOODS.inverse().get(blockSetType);
        return color != null ? settings.mapColor(color) : settings;
    }
}

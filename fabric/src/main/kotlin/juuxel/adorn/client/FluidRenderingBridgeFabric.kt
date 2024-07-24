package juuxel.adorn.client

import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.util.FluidStorageReference
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.texture.Sprite
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

object FluidRenderingBridgeFabric : FluidRenderingBridge {
    @Environment(EnvType.CLIENT)
    override fun getStillSprite(volume: FluidReference): Sprite? =
        FluidVariantRendering.getSprite(FluidStorageReference.toFluidVariant(volume))

    @Environment(EnvType.CLIENT)
    override fun getColor(volume: FluidReference, world: BlockRenderView?, pos: BlockPos?) =
        FluidVariantRendering.getColor(FluidStorageReference.toFluidVariant(volume), world, pos)

    @Environment(EnvType.CLIENT)
    override fun fillsFromTop(volume: FluidReference): Boolean =
        FluidVariantAttributes.isLighterThanAir(FluidStorageReference.toFluidVariant(volume))

    @Environment(EnvType.CLIENT)
    override fun getTooltip(volume: FluidReference, context: TooltipContext, maxAmountInLitres: Int?): List<Text> {
        val result = FluidVariantRendering.getTooltip(FluidStorageReference.toFluidVariant(volume), context).toMutableList()

        if (maxAmountInLitres != null) {
            result.add(1, volume.getAmountText(maxAmountInLitres.toLong(), FluidUnit.LITRE))
        } else {
            result.add(1, volume.getAmountText())
        }

        return result
    }
}

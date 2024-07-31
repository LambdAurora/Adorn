package juuxel.adorn.client

import juuxel.adorn.fluid.FluidReference
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.texture.Sprite
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

interface FluidRenderingBridge {
    fun getStillSprite(volume: FluidReference): Sprite?

    fun getColor(volume: FluidReference, world: BlockRenderView? = null, pos: BlockPos? = null): Int

    fun getColor(volume: FluidReference): Int =
        getColor(volume, null, null)

    fun fillsFromTop(volume: FluidReference): Boolean

    fun getTooltip(volume: FluidReference, context: TooltipContext, maxAmountInLitres: Int?): List<Text>

    companion object {
        @JvmStatic
        fun get() = ClientPlatformBridges.get().fluidRendering
    }
}

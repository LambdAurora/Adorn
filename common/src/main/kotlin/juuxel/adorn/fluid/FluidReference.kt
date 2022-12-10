package juuxel.adorn.fluid

import juuxel.adorn.config.ConfigManager
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import kotlin.math.max

/**
 * A mutable reference to a fluid volume.
 * This can be a [FluidVolume] or a block entity's
 * internal fluid volume.
 */
abstract class FluidReference : HasFluidAmount {
    abstract var fluid: Fluid
    abstract override var amount: Long
    abstract var nbt: NbtCompound?
    val isEmpty: Boolean get() = fluid == Fluids.EMPTY || amount == 0L

    fun write(buf: PacketByteBuf) {
        buf.writeEnumConstant(unit)

        if (isEmpty) {
            buf.writeBoolean(false)
        } else {
            buf.writeBoolean(true)
            buf.writeVarInt(Registries.FLUID.getRawId(fluid))
            buf.writeVarLong(amount)
            buf.writeNbt(nbt)
        }
    }

    protected fun readWithoutUnit(buf: PacketByteBuf) {
        if (buf.readBoolean()) {
            fluid = Registries.FLUID[buf.readVarInt()]
            amount = buf.readVarLong()
            nbt = buf.readNbt()
        } else {
            fluid = Fluids.EMPTY
            amount = 0
            nbt = null
        }
    }

    /**
     * Creates an independent mutable snapshot of this fluid reference's current contents.
     *
     * For fluid volumes, this is the same as creating a standard copy with [FluidVolume.copy].
     */
    fun createSnapshot(): FluidVolume = FluidVolume(fluid, amount, nbt, unit)

    fun increment(amount: Long, unit: FluidUnit) {
        this.amount += FluidUnit.convert(amount, this.unit, unit)
    }

    fun decrement(amount: Long, unit: FluidUnit) =
        increment(-amount, unit)

    fun matches(ingredient: FluidIngredient): Boolean =
        ingredient.fluid.matches(fluid) && FluidUnit.compareVolumes(this, ingredient) >= 0 && nbt == ingredient.nbt

    fun getAmountText(displayUnit: FluidUnit = getDefaultDisplayUnit()): Text =
        Text.translatable(
            "gui.adorn.fluid_volume",
            FluidUnit.losslessConvert(amount, unit, displayUnit).resizeFraction(getUnitDenominator(unit, displayUnit)),
            displayUnit.symbol
        )

    fun getAmountText(max: Long, maxUnit: FluidUnit, displayUnit: FluidUnit = getDefaultDisplayUnit()): Text =
        Text.translatable(
            "gui.adorn.fluid_volume.fraction",
            FluidUnit.losslessConvert(amount, unit, displayUnit).resizeFraction(getUnitDenominator(unit, displayUnit)),
            FluidUnit.losslessConvert(max, maxUnit, displayUnit).resizeFraction(getUnitDenominator(maxUnit, displayUnit)),
            displayUnit.symbol
        )

    override fun toString() =
        "FluidReference(fluid=${Registries.FLUID.getId(fluid)}, amount=$amount, nbt=$nbt)"

    companion object {
        private fun getUnitDenominator(from: FluidUnit, to: FluidUnit): Long {
            if (from.bucketVolume == to.bucketVolume) return 1
            return max(1, from.bucketVolume / to.bucketVolume)
        }

        private fun getDefaultDisplayUnit() =
            ConfigManager.config().client.displayedFluidUnit

        fun areFluidsEqual(a: FluidReference, b: FluidReference): Boolean {
            if (a.isEmpty) return b.isEmpty
            return a.fluid == b.fluid && a.nbt == b.nbt
        }

        fun areFluidsAndAmountsEqual(a: FluidReference, b: FluidReference): Boolean {
            if (a.isEmpty) return b.isEmpty
            return areFluidsEqual(a, b) && FluidUnit.compareVolumes(a, b) == 0
        }
    }
}

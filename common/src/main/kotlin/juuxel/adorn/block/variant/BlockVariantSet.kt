package juuxel.adorn.block.variant

import java.util.function.Function

interface BlockVariantSet {
    val woodVariants: List<BlockVariant> get() = emptyList()
    val stoneVariants: List<BlockVariant> get() = emptyList()

    fun addVariants(consumer: CustomVariantConsumer) {
        // No customised variants by default.
    }

    fun sortVariants(sorter: Sorter) {
        // No custom sorting by default.
    }

    companion object {
        @JvmStatic
        fun createVariants(prefix: String, factory: Function<String, BlockVariant>, vararg variants: String): List<BlockVariant> =
            variants.map { factory.apply("$prefix/$it") }
    }

    fun interface CustomVariantConsumer {
        fun add(variant: BlockVariant, kinds: List<BlockKind>)
    }

    fun interface Sorter {
        fun moveAfter(variant: BlockVariant, after: BlockVariant)
    }
}

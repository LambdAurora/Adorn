package juuxel.adorn.compat

import com.google.common.collect.ImmutableListMultimap
import com.google.common.collect.ListMultimap
import com.google.common.collect.MultimapBuilder
import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.BenchBlock
import juuxel.adorn.block.ChairBlock
import juuxel.adorn.block.CoffeeTableBlock
import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.block.KitchenCounterBlock
import juuxel.adorn.block.KitchenSinkBlock
import juuxel.adorn.block.PlatformBlock
import juuxel.adorn.block.PostBlock
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.block.StepBlock
import juuxel.adorn.block.TableBlock
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.lib.Registered
import juuxel.adorn.lib.RegistryHelper
import net.minecraft.block.Block

class CompatLoader : RegistryHelper() {
    private val modules: MutableList<CompatModule> = ArrayList()
    lateinit var blocksByKind: ListMultimap<BlockKind, Registered<Block>>
        private set

    fun addModule(module: CompatModule) {
        modules += module
    }

    fun register() {
        val woodVariants = modules.flatMap(CompatModule::woodVariants)
        val stoneVariants = modules.flatMap(CompatModule::stoneVariants)
        val allVariants = woodVariants + stoneVariants
        val result: ListMultimap<BlockKind, Registered<Block>> = MultimapBuilder.treeKeys().arrayListValues().build()

        result.putAll(BlockKind.POST, allVariants.map(::registerPost))
        result.putAll(BlockKind.PLATFORM, allVariants.map(::registerPlatform))
        result.putAll(BlockKind.STEP, allVariants.map(::registerStep))
        result.putAll(BlockKind.DRAWER, woodVariants.map(::registerDrawer))
        result.putAll(BlockKind.CHAIR, woodVariants.map(::registerChair))
        result.putAll(BlockKind.TABLE, woodVariants.map(::registerTable))
        result.putAll(BlockKind.KITCHEN_COUNTER, woodVariants.map(::registerKitchenCounter))
        result.putAll(BlockKind.KITCHEN_CUPBOARD, woodVariants.map(::registerKitchenCupboard))
        result.putAll(BlockKind.KITCHEN_SINK, woodVariants.map(::registerKitchenSink))
        result.putAll(BlockKind.SHELF, woodVariants.map(::registerShelf))
        result.putAll(BlockKind.COFFEE_TABLE, woodVariants.map(::registerCoffeeTable))
        result.putAll(BlockKind.BENCH, woodVariants.map(::registerBench))

        blocksByKind = result
    }

    private fun registerPost(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_post") { PostBlock(variant) }

    private fun registerPlatform(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_platform") { PlatformBlock(variant) }

    private fun registerStep(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_step") { StepBlock(variant) }

    private fun registerDrawer(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_drawer") { DrawerBlock(variant) }

    private fun registerChair(variant: BlockVariant): Registered<Block> {
        val block = registerBlock("${variant.name}_table") { ChairBlock(variant) }
        registerItem("${variant.name}_table") { ChairBlockItem(block.get()) }
        return block
    }

    private fun registerTable(variant: BlockVariant): Registered<Block> {
        val block = registerBlock("${variant.name}_table") { TableBlock(variant) }
        registerItem("${variant.name}_table") { TableBlockItem(block.get()) }
        return block
    }

    private fun registerKitchenCounter(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_kitchen_counter") { KitchenCounterBlock(variant) }

    private fun registerKitchenCupboard(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_kitchen_cupboard") { KitchenCounterBlock(variant) }

    private fun registerKitchenSink(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_kitchen_sink") { KitchenSinkBlock(variant) }

    private fun registerShelf(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_shelf") { ShelfBlock(variant) }

    private fun registerCoffeeTable(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_coffee_table") { CoffeeTableBlock(variant) }

    private fun registerBench(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_bench") { BenchBlock(variant) }
}

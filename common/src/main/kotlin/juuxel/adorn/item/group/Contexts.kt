package juuxel.adorn.item.group

import juuxel.adorn.lib.registry.Registered
import net.minecraft.item.ItemConvertible

fun interface ItemGroupBuildContext {
    fun add(item: ItemConvertible)

    fun add(item: Registered<ItemConvertible>) {
        add(item.get())
    }
}

interface ItemGroupModifyContext : ItemGroupBuildContext {
    fun addAfter(after: ItemConvertible, items: List<ItemConvertible>)

    fun addAfter(after: ItemConvertible, item: ItemConvertible) {
        addAfter(after, listOf(item))
    }

    fun addAfter(after: ItemConvertible, item: Registered<ItemConvertible>) {
        addAfter(after, item.get())
    }
}

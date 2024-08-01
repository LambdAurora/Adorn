package juuxel.adorn.platform

import juuxel.adorn.item.group.ItemGroupModifyContext
import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import java.util.function.Consumer

interface ItemGroupBridge {
    fun builder(): ItemGroup.Builder
    fun addItems(group: RegistryKey<ItemGroup>, configurator: ItemGroupModifyContext.() -> Unit)

    fun addItems(group: RegistryKey<ItemGroup>, configurator: Consumer<ItemGroupModifyContext>) {
        addItems(group, configurator::accept)
    }

    @InlineServices
    companion object {
        private val instance: ItemGroupBridge by lazy { loadService() }

        @JvmStatic
        fun get(): ItemGroupBridge = instance
    }
}

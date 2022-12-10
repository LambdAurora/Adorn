package juuxel.adorn.platform.fabric

import juuxel.adorn.lib.Registered
import juuxel.adorn.platform.ItemGroupBridge
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier

class ItemGroupBridgeFabric : ItemGroupBridge {
    override fun register(id: Identifier, configurator: ItemGroup.Builder.() -> Unit): Registered<ItemGroup> {
        val itemGroup = FabricItemGroup.builder(id)
            .apply(configurator)
            .build()
        return Registered { itemGroup }
    }

    override fun addItems(group: ItemGroup, configurator: ItemGroupBridge.ItemGroupContext.() -> Unit) {
        ItemGroupEvents.modifyEntriesEvent(group).register { entries ->
            val context = object : ItemGroupBridge.ItemGroupContext {
                override fun add(item: ItemConvertible) {
                    entries.add(item)
                }
            }

            configurator(context)
        }
    }
}

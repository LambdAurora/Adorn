package juuxel.adorn.platform.fabric;

import juuxel.adorn.item.group.ItemGroupModifyContext;
import juuxel.adorn.platform.ItemGroupBridge;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;

import java.util.List;

public final class ItemGroupBridgeFabric implements ItemGroupBridge {
    @Override
    public ItemGroup.Builder builder() {
        return FabricItemGroup.builder();
    }

    @Override
    public void addItems(RegistryKey<ItemGroup> group, Function1<? super ItemGroupModifyContext, Unit> configurator) {
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> {
            var context = new ItemGroupModifyContext() {
                @Override
                public void add(ItemConvertible item) {
                    entries.add(item);
                }

                @Override
                public void addAfter(ItemConvertible after, List<? extends ItemConvertible> items) {
                    entries.addAfter(after, items.toArray(ItemConvertible[]::new));
                }
            };

            configurator.invoke(context);
        });
    }
}

package juuxel.adorn.item.group;

import juuxel.adorn.lib.registry.Registered;
import net.minecraft.item.ItemConvertible;

import java.util.List;

public interface ItemGroupModifyContext extends ItemGroupBuildContext {
    void addAfter(ItemConvertible after, List<? extends ItemConvertible> items);

    default void addAfter(ItemConvertible after, ItemConvertible item) {
        addAfter(after, List.of(item));
    }

    default void addAfter(ItemConvertible after, Registered<? extends ItemConvertible> item) {
        addAfter(after, item.get());
    }
}

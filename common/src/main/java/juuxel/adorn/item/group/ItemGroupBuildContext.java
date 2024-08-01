package juuxel.adorn.item.group;

import juuxel.adorn.lib.registry.Registered;
import net.minecraft.item.ItemConvertible;

@FunctionalInterface
public interface ItemGroupBuildContext {
    void add(ItemConvertible item);

    default void add(Registered<? extends ItemConvertible> item) {
        add(item.get());
    }
}

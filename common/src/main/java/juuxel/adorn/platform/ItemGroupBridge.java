package juuxel.adorn.platform;

import juuxel.adorn.item.group.ItemGroupModifyContext;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;

import java.util.function.Consumer;

@InlineServices
public interface ItemGroupBridge {
    ItemGroup.Builder builder();
    void addItems(RegistryKey<ItemGroup> group, Consumer<ItemGroupModifyContext> configurator);

    @InlineServices.Getter
    static ItemGroupBridge get() {
        return Services.load(ItemGroupBridge.class);
    }
}

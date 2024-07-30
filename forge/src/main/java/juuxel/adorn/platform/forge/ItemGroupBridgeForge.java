package juuxel.adorn.platform.forge;

import com.mojang.datafixers.util.Pair;
import juuxel.adorn.item.group.ItemGroupModifyContext;
import juuxel.adorn.platform.ItemGroupBridge;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.ArrayList;
import java.util.List;

public final class ItemGroupBridgeForge implements ItemGroupBridge {
    private static final ItemGroup.StackVisibility DEFAULT_STACK_VISIBILITY = ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS;
    private final List<Pair<RegistryKey<ItemGroup>, Function1<? super ItemGroupModifyContext, Unit>>> additions = new ArrayList<>();

    @Override
    public ItemGroup.Builder builder() {
        return ItemGroup.builder();
    }

    @Override
    public void addItems(RegistryKey<ItemGroup> group, Function1<? super ItemGroupModifyContext, Unit> configurator) {
        additions.add(new Pair<>(group, configurator));
    }

    @SubscribeEvent
    public void addToGroups(BuildCreativeModeTabContentsEvent event) {
        for (var entry : additions) {
            var group = entry.getFirst();
            var configurator = entry.getSecond();
            var context = new ItemGroupModifyContext() {
                @Override
                public void add(ItemConvertible item) {
                    if (event.getTabKey().equals(group)) {
                        event.add(item);
                    }
                }

                @Override
                public void addAfter(ItemConvertible after, List<? extends ItemConvertible> items) {
                    if (event.getTabKey().equals(group)) {
                        var afterStack = new ItemStack(after);
                        for (ItemConvertible item : items) {
                            var stack = new ItemStack(item);
                            event.getEntries().putAfter(afterStack, stack, DEFAULT_STACK_VISIBILITY);
                            afterStack = stack;
                        }
                    }
                }
            };
            configurator.invoke(context);
        }
    }
}

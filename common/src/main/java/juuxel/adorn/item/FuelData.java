package juuxel.adorn.item;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.lib.AdornTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

import java.util.Set;

public sealed interface FuelData {
    Set<FuelData> FUEL_DATA = Set.of(
        // Wooden (300)
        new ForTag(AdornTags.CHAIRS.item(), 300),
        new ForTag(AdornTags.DRAWERS.item(), 300),
        new ForTag(AdornTags.TABLES.item(), 300),
        new ForTag(AdornTags.BENCHES.item(), 300),
        new ForTag(AdornTags.WOODEN_POSTS.item(), 300),
        new ForTag(AdornTags.WOODEN_PLATFORMS.item(), 300),
        new ForTag(AdornTags.WOODEN_STEPS.item(), 300),
        new ForTag(AdornTags.WOODEN_SHELVES.item(), 300),
        new ForItem(AdornBlocks.INSTANCE.getCRATE(), 300),
        // Woollen (150)
        new ForTag(AdornTags.SOFAS.item(), 150)
    );

    int burnTime();
    boolean matches(ItemStack stack);

    record ForItem(ItemConvertible item, int burnTime) implements FuelData {
        @Override
        public boolean matches(ItemStack stack) {
            return stack.isOf(item.asItem());
        }
    }

    record ForTag(TagKey<Item> tag, int burnTime) implements FuelData {
        @Override
        public boolean matches(ItemStack stack) {
            return stack.isIn(tag);
        }
    }
}

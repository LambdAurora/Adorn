package juuxel.adorn.lib.registry;

import juuxel.adorn.item.BaseBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public final class RegistryHelper {
    private final Registrar<Block> blocks;
    private final Registrar<Item> items;

    public RegistryHelper(Registrar<Block> blocks, Registrar<Item> items) {
        this.blocks = blocks;
        this.items = items;
    }

    // ----------------------------------
    // Functions for registering blocks
    // ----------------------------------

    /**
     * Registers a block with the name and an item with default settings.
     */
    public <T extends Block> Registered<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, Item.Settings::new, block);
    }

    /**
     * Registers a block with the name and the item settings.
     */
    public <T extends Block> Registered<T> registerBlock(String name, Supplier<Item.Settings> itemSettings, Supplier<T> block) {
        return registerBlock(name, b -> makeItemForBlock(b, itemSettings.get()), block);
    }

    /**
     * Registers a block with the name and an item created by the item provider.
     */
    public <T extends Block> Registered<T> registerBlock(String name, Function<T, Item> itemProvider, Supplier<T> block) {
        var registered = registerBlockWithoutItem(name, block);
        registerItem(name, () -> itemProvider.apply(registered.get()));
        return registered;
    }

    /**
     * Registers a block with the name and without an item.
     */
    public <T extends Block> Registered<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        // TODO: no method ref
        return blocks.register(name, block::get);
    }

    private Item makeItemForBlock(Block block, Item.Settings itemSettings) {
        return new BaseBlockItem(block, itemSettings);
    }

    // -----------------------------------------
    // Functions for registering other content
    // -----------------------------------------

    public <T extends Item> Registered<T> registerItem(String name, Supplier<T> content) {
        // TODO: no method ref
        return items.register(name, content::get);
    }
}

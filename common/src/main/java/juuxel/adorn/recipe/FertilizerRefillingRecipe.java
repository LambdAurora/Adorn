package juuxel.adorn.recipe;

import juuxel.adorn.item.AdornItems;
import juuxel.adorn.item.WateringCanItem;
import juuxel.adorn.lib.AdornTags;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class FertilizerRefillingRecipe extends SpecialCraftingRecipe {
    public FertilizerRefillingRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        return match(inventory) != null;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        var match = match(inventory);
        if (match == null) return ItemStack.EMPTY;

        var result = match.wateringCan().copy();
        var nbt = result.getOrCreateNbt();
        var fertilizerLevel = nbt.getInt(WateringCanItem.NBT_FERTILIZER_LEVEL);
        var newFertilizerLevel = Math.min(fertilizerLevel + match.fertilizers(), WateringCanItem.MAX_FERTILIZER_LEVEL);
        nbt.putInt(WateringCanItem.NBT_FERTILIZER_LEVEL, newFertilizerLevel);
        return result;
    }

    private @Nullable MatchResult match(RecipeInputInventory inventory) {
        var wateringCan = ItemStack.EMPTY;
        var fertilizers = 0;

        for (int slot = 0; slot < inventory.size(); slot++) {
            var stack = inventory.getStack(slot);

            if (stack.isOf(AdornItems.WATERING_CAN.get())) {
                if (wateringCan.isEmpty()) {
                    wateringCan = stack;
                } else {
                    // We don't want double watering cans
                    return null;
                }
            } else if (!stack.isEmpty()) {
                if (stack.isIn(AdornTags.WATERING_CAN_FERTILIZERS)) {
                    fertilizers++;
                } else {
                    // Unwanted item
                    return null;
                }
            }
        }

        // Not a successful match if we don't have a watering can at all or no fertilizer to fill it with
        if (wateringCan.isEmpty() || fertilizers == 0) return null;

        return new MatchResult(wateringCan, fertilizers);
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AdornRecipes.FERTILIZER_REFILLING_SERIALIZER.get();
    }

    private record MatchResult(ItemStack wateringCan, int fertilizers) {
    }
}

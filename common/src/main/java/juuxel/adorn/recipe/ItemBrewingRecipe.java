package juuxel.adorn.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;

import static juuxel.adorn.block.entity.BrewerBlockEntity.LEFT_INGREDIENT_SLOT;
import static juuxel.adorn.block.entity.BrewerBlockEntity.RIGHT_INGREDIENT_SLOT;

public record ItemBrewingRecipe(Ingredient firstIngredient, Ingredient secondIngredient, ItemStack result) implements BrewingRecipe {
    public static Codec<ItemBrewingRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(
        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("first_ingredient").forGetter(ItemBrewingRecipe::firstIngredient),
        Ingredient.ALLOW_EMPTY_CODEC.optionalFieldOf("second_ingredient", Ingredient.empty()).forGetter(ItemBrewingRecipe::secondIngredient),
        ItemStack.RECIPE_RESULT_CODEC.fieldOf("result").forGetter(ItemBrewingRecipe::result)
    ).apply(builder, ItemBrewingRecipe::new));

    @Override
    public boolean matches(BrewerInventory inventory, World world) {
        return (matches(inventory, LEFT_INGREDIENT_SLOT, firstIngredient) && matches(inventory, RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (matches(inventory, RIGHT_INGREDIENT_SLOT, firstIngredient) && matches(inventory, LEFT_INGREDIENT_SLOT, secondIngredient));
    }

    private static boolean matches(BrewerInventory inventory, int index, Ingredient ingredient) {
        return ingredient.test(inventory.getStack(index));
    }

    @Override
    public ItemStack craft(BrewerInventory inventory, DynamicRegistryManager registryManager) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AdornRecipes.BREWING_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<ItemBrewingRecipe> {
        @Override
        public Codec<ItemBrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public ItemBrewingRecipe read(PacketByteBuf buf) {
            var first = Ingredient.fromPacket(buf);
            var second = Ingredient.fromPacket(buf);
            var output = buf.readItemStack();
            return new ItemBrewingRecipe(first, second, output);
        }

        @Override
        public void write(PacketByteBuf buf, ItemBrewingRecipe recipe) {
            recipe.firstIngredient.write(buf);
            recipe.secondIngredient.write(buf);
            buf.writeItemStack(recipe.result);
        }
    }
}

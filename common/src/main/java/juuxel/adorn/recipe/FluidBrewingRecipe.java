package juuxel.adorn.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.fluid.FluidIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;

import static juuxel.adorn.block.entity.BrewerBlockEntity.LEFT_INGREDIENT_SLOT;
import static juuxel.adorn.block.entity.BrewerBlockEntity.RIGHT_INGREDIENT_SLOT;

public record FluidBrewingRecipe(Ingredient firstIngredient, Ingredient secondIngredient, FluidIngredient fluid, ItemStack result) implements BrewingRecipe {
    public static final Codec<FluidBrewingRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(
        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("first_ingredient").forGetter(FluidBrewingRecipe::firstIngredient),
        Ingredient.ALLOW_EMPTY_CODEC.optionalFieldOf("second_ingredient", Ingredient.empty()).forGetter(FluidBrewingRecipe::secondIngredient),
        FluidIngredient.CODEC.fieldOf("fluid").forGetter(FluidBrewingRecipe::fluid),
        ItemStack.RECIPE_RESULT_CODEC.fieldOf("result").forGetter(FluidBrewingRecipe::result)
    ).apply(builder, FluidBrewingRecipe::new));

    @Override
    public boolean matches(BrewerInventory inventory, World world) {
        var itemsMatch = (matches(inventory, LEFT_INGREDIENT_SLOT, firstIngredient) && matches(inventory, RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (matches(inventory, RIGHT_INGREDIENT_SLOT, firstIngredient) && matches(inventory, LEFT_INGREDIENT_SLOT, firstIngredient));
        return itemsMatch && inventory.getFluidReference().matches(fluid);
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
        return AdornRecipes.BREWING_FROM_FLUID_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<FluidBrewingRecipe> {
        @Override
        public Codec<FluidBrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public FluidBrewingRecipe read(PacketByteBuf buf) {
            var first = Ingredient.fromPacket(buf);
            var second = Ingredient.fromPacket(buf);
            var fluid = FluidIngredient.load(buf);
            var output = buf.readItemStack();
            return new FluidBrewingRecipe(first, second, fluid, output);
        }

        @Override
        public void write(PacketByteBuf buf, FluidBrewingRecipe recipe) {
            recipe.firstIngredient.write(buf);
            recipe.secondIngredient.write(buf);
            recipe.fluid.write(buf);
            buf.writeItemStack(recipe.result);
        }
    }
}

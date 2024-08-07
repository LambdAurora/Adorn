package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import static juuxel.adorn.block.entity.BrewerBlockEntity.LEFT_INGREDIENT_SLOT;
import static juuxel.adorn.block.entity.BrewerBlockEntity.RIGHT_INGREDIENT_SLOT;

public record ItemBrewingRecipe(Ingredient firstIngredient, Ingredient secondIngredient, ItemStack result) implements BrewingRecipe {
    public static final MapCodec<ItemBrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("first_ingredient").forGetter(ItemBrewingRecipe::firstIngredient),
        Ingredient.ALLOW_EMPTY_CODEC.optionalFieldOf("second_ingredient", Ingredient.empty()).forGetter(ItemBrewingRecipe::secondIngredient),
        ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(ItemBrewingRecipe::result)
    ).apply(builder, ItemBrewingRecipe::new));

    @Override
    public boolean matches(BrewerInput input, World world) {
        return (matches(input, LEFT_INGREDIENT_SLOT, firstIngredient) && matches(input, RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (matches(input, RIGHT_INGREDIENT_SLOT, firstIngredient) && matches(input, LEFT_INGREDIENT_SLOT, secondIngredient));
    }

    private static boolean matches(BrewerInput input, int index, Ingredient ingredient) {
        return ingredient.test(input.getStackInSlot(index));
    }

    @Override
    public ItemStack craft(BrewerInput input, RegistryWrapper.WrapperLookup registries) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AdornRecipes.BREWING_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<ItemBrewingRecipe> {
        private static final PacketCodec<RegistryByteBuf, ItemBrewingRecipe> PACKET_CODEC =
            PacketCodec.ofStatic(Serializer::write, Serializer::read);

        @Override
        public MapCodec<ItemBrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ItemBrewingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static ItemBrewingRecipe read(RegistryByteBuf buf) {
            var first = Ingredient.PACKET_CODEC.decode(buf);
            var second = Ingredient.PACKET_CODEC.decode(buf);
            var output = ItemStack.PACKET_CODEC.decode(buf);
            return new ItemBrewingRecipe(first, second, output);
        }

        private static void write(RegistryByteBuf buf, ItemBrewingRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.firstIngredient);
            Ingredient.PACKET_CODEC.encode(buf, recipe.secondIngredient);
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
        }
    }
}

package juuxel.adorn.compat.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.platform.FluidBridge;
import net.minecraft.item.Item;

public final class EmiUtil {
    public static EmiIngredient emiIngredientOf(FluidIngredient ingredient) {
        long amount = FluidUnit.convert(ingredient.amount(), ingredient.unit(), FluidBridge.get().getFluidUnit());
        return EmiIngredient.of(
            ingredient.fluid()
                .getFluids()
                .stream()
                .map(fluid -> EmiStack.of(fluid, ingredient.nbt(), amount))
                .toList()
        );
    }

    public static EmiIngredient withRemainders(EmiIngredient ingredient) {
        for (var stack : ingredient.getEmiStacks()) {
            var item = stack.getKeyOfType(Item.class);
            if (item == null) continue;
            if (item.hasRecipeRemainder()) {
                // TODO: Use stack-aware recipe remainders
                stack.setRemainder(EmiStack.of(item.getRecipeRemainder()));
            }
        }

        return ingredient;
    }
}

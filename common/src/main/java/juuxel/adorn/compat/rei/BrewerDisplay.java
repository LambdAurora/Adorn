package juuxel.adorn.compat.rei;

import dev.architectury.fluid.FluidStack;
import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.platform.FluidBridge;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.ItemBrewingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.List;

public record BrewerDisplay(
    EntryIngredient input,
    EntryIngredient first,
    EntryIngredient second,
    EntryIngredient fluid,
    EntryStack<?> result
) implements Display {
    public BrewerDisplay(ItemBrewingRecipe recipe) {
        this(
            EntryIngredients.of(AdornItems.MUG.get()),
            EntryIngredients.ofIngredient(recipe.firstIngredient()),
            EntryIngredients.ofIngredient(recipe.secondIngredient()),
            EntryIngredient.empty(),
            EntryStacks.of(recipe.result())
        );
    }

    public BrewerDisplay(FluidBrewingRecipe recipe) {
        this(
            EntryIngredients.of(AdornItems.MUG.get()),
            EntryIngredients.ofIngredient(recipe.firstIngredient()),
            EntryIngredients.ofIngredient(recipe.secondIngredient()),
            entryIngredientOf(recipe.fluid()),
            EntryStacks.of(recipe.result())
        );
    }

    private static EntryIngredient entryIngredientOf(FluidIngredient fluidIngredient) {
        var amount = FluidUnit.convert(fluidIngredient.getAmount(), fluidIngredient.getUnit(), FluidBridge.get().getFluidUnit());
        var stacks = fluidIngredient.fluid()
            .getFluids()
            .stream()
            .map(fluid -> EntryStacks.of(FluidStack.create(fluid, amount, fluidIngredient.nbt())))
            .toList();
        return EntryIngredient.of(stacks);
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(input, first, second, fluid);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(EntryIngredient.of(result));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return AdornReiServer.BREWER;
    }

    public static final class Serializer implements DisplaySerializer<BrewerDisplay> {
        @Override
        public NbtCompound save(NbtCompound tag, BrewerDisplay display) {
            tag.put("Input", display.input.saveIngredient());
            tag.put("FirstIngredient", display.first.saveIngredient());
            tag.put("SecondIngredient", display.second.saveIngredient());
            tag.put("Fluid", display.fluid.saveIngredient());
            tag.put("Result", display.result.saveStack());
            return tag;
        }

        @Override
        public BrewerDisplay read(NbtCompound tag) {
            var input = EntryIngredient.read(tag.getList("Input", NbtElement.COMPOUND_TYPE));
            var first = EntryIngredient.read(tag.getList("FirstIngredient", NbtElement.COMPOUND_TYPE));
            var second = EntryIngredient.read(tag.getList("SecondIngredient", NbtElement.COMPOUND_TYPE));
            var fluid = EntryIngredient.read(tag.getList("Fluid", NbtElement.COMPOUND_TYPE));
            var result = EntryStack.read(tag.getCompound("Result"));
            return new BrewerDisplay(input, first, second, fluid, result);
        }
    }
}

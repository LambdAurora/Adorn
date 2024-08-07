package juuxel.adorn.item;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.component.AdornComponentTypes;
import net.minecraft.component.type.FoodComponent;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

public final class AdornItems {
    public static final Registrar<Item> ITEMS = RegistrarFactory.get().create(RegistryKeys.ITEM);
    private static final FoodComponent DRINK_FOOD_COMPONENT = drinkFoodComponentBuilder().build();

    public static final Registered<Item> STONE_ROD = ITEMS.register("stone_rod", () -> new ItemWithDescription(new Item.Settings()));
    public static final Registered<Item> MUG = ITEMS.register("mug", () -> new ItemWithDescription(new Item.Settings().maxCount(16)));
    public static final Registered<Item> HOT_CHOCOLATE = ITEMS.register("hot_chocolate",
        () -> new DrinkInMugItem(new Item.Settings().food(DRINK_FOOD_COMPONENT).maxCount(1)));
    public static final Registered<Item> SWEET_BERRY_JUICE = ITEMS.register("sweet_berry_juice",
        () -> new DrinkInMugItem(new Item.Settings().food(DRINK_FOOD_COMPONENT).maxCount(1)));
    public static final Registered<Item> GLOW_BERRY_TEA = ITEMS.register("glow_berry_tea",
        () -> new DrinkInMugItem(
            new Item.Settings()
                .food(drinkFoodComponentBuilder().statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 400), 1.0f).build())
                .maxCount(1)
        ));
    public static final Registered<Item> NETHER_WART_COFFEE = ITEMS.register("nether_wart_coffee",
        () -> new DrinkInMugItem(new Item.Settings().food(DRINK_FOOD_COMPONENT).maxCount(1)));

    public static final Registered<Item> STONE_TORCH = ITEMS.register("stone_torch",
        () -> new VerticallyAttachableBlockItemWithDescription(
            AdornBlocks.STONE_TORCH_GROUND.get(),
            AdornBlocks.STONE_TORCH_WALL.get(),
            new Item.Settings(),
            Direction.DOWN
        ));

    public static final Registered<Item> GUIDE_BOOK = ITEMS.register("guide_book",
        () -> new AdornBookItem(AdornCommon.id("guide"), new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Registered<Item> TRADERS_MANUAL = ITEMS.register("traders_manual",
        () -> new AdornBookItem(AdornCommon.id("traders_manual"), new Item.Settings()));

    public static final Registered<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new ItemWithDescription(new Item.Settings()));
    public static final Registered<Item> WATERING_CAN = ITEMS.register("watering_can",
        () -> new WateringCanItem(
            new Item.Settings()
                .component(AdornComponentTypes.FERTILIZER_LEVEL.get(), 0)
                .component(AdornComponentTypes.WATER_LEVEL.get(), 0)
        ));

    private static FoodComponent.Builder drinkFoodComponentBuilder() {
        return new FoodComponent.Builder().nutrition(4).saturationModifier(0.3F).alwaysEdible();
    }

    public static void init() {
    }
}

package juuxel.adorn.menu;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.platform.PlatformBridges;
import kotlin.jvm.functions.Function3;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.menu.Menu;
import net.minecraft.menu.MenuType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;

public final class AdornMenus {
    public static final Registrar<MenuType<?>> MENUS = RegistrarFactory.get().create(RegistryKeys.SCREEN_HANDLER);
    public static final Registered<MenuType<DrawerMenu>> DRAWER =
        MENUS.register("drawer", () -> createType(DrawerMenu::load));
    public static final Registered<MenuType<KitchenCupboardMenu>> KITCHEN_CUPBOARD =
        MENUS.register("kitchen_cupboard", () -> createType(KitchenCupboardMenu::load));
    public static final Registered<MenuType<TradingStationMenu>> TRADING_STATION =
        MENUS.register("trading_station", () -> new MenuType<>(TradingStationMenu::new, FeatureFlags.VANILLA_FEATURES));
    public static final Registered<MenuType<BrewerMenu>> BREWER =
        MENUS.register("brewer", () -> new MenuType<>(BrewerMenu::new, FeatureFlags.VANILLA_FEATURES));

    public static void init() {
    }

    private static <M extends Menu> MenuType<M> createType(Function3<Integer, PlayerInventory, PacketByteBuf, M> factory) {
        return PlatformBridges.Companion.getMenus().createType(factory);
    }
}

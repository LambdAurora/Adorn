package juuxel.adorn.client.gui.screen;

import juuxel.adorn.menu.AdornMenus;
import net.minecraft.client.gui.screen.ingame.MenuScreens;

public final class AdornMenuScreens {
    public static void register() {
        MenuScreens.register(AdornMenus.INSTANCE.getDRAWER(), DrawerScreen::new);
        MenuScreens.register(AdornMenus.INSTANCE.getKITCHEN_CUPBOARD(), KitchenCupboardScreen::new);
        MenuScreens.register(AdornMenus.INSTANCE.getTRADING_STATION(), TradingStationScreen::new);
        MenuScreens.register(AdornMenus.INSTANCE.getBREWER(), BrewerScreen::new);
    }
}

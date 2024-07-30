package juuxel.adorn.platform.forge.event;

import juuxel.adorn.item.FuelData;
import juuxel.adorn.platform.ItemGroupBridge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

public final class ItemEvents {
    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(ItemEvents::onFuelTime);
        modBus.register(ItemGroupBridge.get());
    }

    private static void onFuelTime(FurnaceFuelBurnTimeEvent event) {
        for (var fuelData : FuelData.FUEL_DATA) {
            if (fuelData.matches(event.getItemStack())) {
                event.setBurnTime(fuelData.burnTime());
                break;
            }
        }
    }
}

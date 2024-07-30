package juuxel.adorn.lib;

import juuxel.adorn.item.FuelData;
import net.fabricmc.fabric.api.registry.FuelRegistry;

public final class AdornItemsFabric {
    public static void init() {
        for (var fuelData : FuelData.FUEL_DATA) {
            switch (fuelData) {
                case FuelData.ForItem(var item, int burnTime) -> FuelRegistry.INSTANCE.add(item.get(), burnTime);
                case FuelData.ForTag(var tag, int burnTime) -> FuelRegistry.INSTANCE.add(tag, burnTime);
            }
        }
    }
}

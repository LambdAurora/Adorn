package juuxel.adorn.lib;

import juuxel.adorn.item.FuelData;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

public final class AdornItemsFabric {
    public static void init() {
        FuelRegistryEvents.BUILD.register((builder, context) -> {
            for (var fuelData : FuelData.FUEL_DATA) {
                switch (fuelData) {
                    case FuelData.ForItem(var item, int burnTime) -> builder.add(item.get(), burnTime);
                    case FuelData.ForTag(var tag, int burnTime) -> builder.add(tag, burnTime);
                }
            }
        });
    }
}

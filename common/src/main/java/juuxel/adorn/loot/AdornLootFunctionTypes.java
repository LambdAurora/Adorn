package juuxel.adorn.loot;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.RegistryKeys;

public final class AdornLootFunctionTypes {
    public static final Registrar<LootFunctionType> LOOT_FUNCTION_TYPES = RegistrarFactory.get().create(RegistryKeys.LOOT_FUNCTION_TYPE);
    public static final Registered<LootFunctionType> CHECK_TRADING_STATION_OWNER =
        LOOT_FUNCTION_TYPES.register("check_trading_station_owner", () -> new LootFunctionType(CheckTradingStationOwnerLootFunction.CODEC));

    public static void init() {
    }
}

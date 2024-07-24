package juuxel.adorn.criterion;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.RegistryKeys;

public final class AdornCriteria {
    public static final Registrar<Criterion<?>> CRITERIA = RegistrarFactory.get().create(RegistryKeys.CRITERION);

    public static final Registered<SitOnBlockCriterion> SIT_ON_BLOCK =
        CRITERIA.register("sit_on_block", SitOnBlockCriterion::new);
    public static final Registered<BoughtFromTradingStationCriterion> BOUGHT_FROM_TRADING_STATION =
        CRITERIA.register("bought_from_trading_station", BoughtFromTradingStationCriterion::new);

    public static void init() {
    }
}

package juuxel.adorn.lib;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.config.Config;
import juuxel.adorn.config.ConfigManager;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.Key;
import net.minecraft.world.GameRules.Rule;
import net.minecraft.world.GameRules.Type;

import java.util.function.Predicate;

public final class AdornGameRules {
    public static final Key<BooleanRule> SKIP_NIGHT_ON_SOFAS =
        register("skipNightOnSofas", Category.PLAYER, createBooleanRule(defaults -> defaults.skipNightOnSofas));
    public static final Key<BooleanRule> INFINITE_KITCHEN_SINKS =
        register("infiniteKitchenSinks", Category.MISC, createBooleanRule(defaults -> defaults.infiniteKitchenSinks));
    public static final Key<BooleanRule> DROP_LOCKED_TRADING_STATIONS =
        register("dropLockedTradingStations", Category.DROPS, createBooleanRule(defaults -> defaults.dropLockedTradingStations));

    public static void init() {
    }

    private static Type<BooleanRule> createBooleanRule(Predicate<Config.GameRuleDefaults> defaultGetter) {
        return BooleanRule.create(defaultGetter.test(ConfigManager.Companion.config().gameRuleDefaults));
    }

    private static <T extends Rule<T>> Key<T> register(String name, Category category, Type<T> type) {
        return GameRules.register(AdornCommon.NAMESPACE + ':' + name, category, type);
    }
}

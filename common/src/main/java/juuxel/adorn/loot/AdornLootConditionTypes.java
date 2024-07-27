package juuxel.adorn.loot;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.RegistryKeys;

public final class AdornLootConditionTypes {
    public static final Registrar<LootConditionType> LOOT_CONDITION_TYPES = RegistrarFactory.get().create(RegistryKeys.LOOT_CONDITION_TYPE);
    public static final Registered<LootConditionType> GAME_RULE =
        LOOT_CONDITION_TYPES.register("game_rule", () -> new LootConditionType(GameRuleLootCondition.CODEC));

    public static void init() {
    }
}

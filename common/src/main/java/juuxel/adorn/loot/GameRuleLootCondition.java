package juuxel.adorn.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.util.Logging;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;

public record GameRuleLootCondition(GameRules.Key<?> gameRule) implements LootCondition {
    public static final MapCodec<GameRuleLootCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        Codec.STRING
            .<GameRules.Key<?>>xmap(name -> new GameRules.Key<>(name, GameRules.Category.MISC), GameRules.Key::getName)
            .fieldOf("game_rule")
            .forGetter(GameRuleLootCondition::gameRule)
    ).apply(builder, GameRuleLootCondition::new));
    private static final Logger LOGGER = Logging.logger();

    @Override
    public boolean test(LootContext lootContext) {
        var rule = lootContext.getWorld().getGameRules().get(gameRule);

        if (rule instanceof GameRules.BooleanRule booleanRule) {
            return booleanRule.get();
        } else if (rule == null) {
            LOGGER.error("Unknown game rule {} in loot condition", gameRule);
        } else {
            LOGGER.error("Game rule {} ({}) is not a boolean", rule, gameRule);
        }

        return false;
    }

    @Override
    public LootConditionType getType() {
        return AdornLootConditionTypes.GAME_RULE.get();
    }
}

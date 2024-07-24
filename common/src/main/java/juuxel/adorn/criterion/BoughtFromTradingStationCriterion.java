package juuxel.adorn.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public final class BoughtFromTradingStationCriterion extends AbstractCriterion<BoughtFromTradingStationCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, ItemStack soldItem) {
        trigger(player, conditions -> conditions.matches(soldItem));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> soldItem) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player")
                .forGetter(Conditions::player),
            Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "item")
                .forGetter(Conditions::soldItem)
        ).apply(instance, Conditions::new));

        public boolean matches(ItemStack stack) {
            return soldItem.map(predicate -> predicate.test(stack)).orElse(true);
        }
    }
}

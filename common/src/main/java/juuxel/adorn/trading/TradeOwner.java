package juuxel.adorn.trading;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record TradeOwner(UUID uuid, Text name) {
    public static final Codec<TradeOwner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Uuids.CODEC.fieldOf("uuid").forGetter(TradeOwner::uuid),
        TextCodecs.CODEC.fieldOf("name").forGetter(TradeOwner::name)
    ).apply(instance, TradeOwner::new));
}

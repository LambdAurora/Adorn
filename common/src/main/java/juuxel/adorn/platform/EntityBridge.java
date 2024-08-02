package juuxel.adorn.platform;

import juuxel.adorn.entity.SeatEntity;
import net.minecraft.entity.EntityType;

public interface EntityBridge {
    EntityType<SeatEntity> createSeatType();
}

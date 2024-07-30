package juuxel.adorn.platform.forge;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.entity.SeatEntity;
import juuxel.adorn.platform.EntityBridge;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public final class EntityBridgeImpl implements EntityBridge {
    public static final EntityBridgeImpl INSTANCE = new EntityBridgeImpl();

    @Override
    public EntityType<SeatEntity> createSeatType() {
        return EntityType.Builder.create(SeatEntity::new, SpawnGroup.MISC)
            .setDimensions(0, 0)
            .build(AdornCommon.NAMESPACE + ":seat");
    }
}

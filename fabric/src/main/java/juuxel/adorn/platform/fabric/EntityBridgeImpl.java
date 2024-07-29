package juuxel.adorn.platform.fabric;

import juuxel.adorn.entity.SeatEntity;
import juuxel.adorn.platform.EntityBridge;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public final class EntityBridgeImpl implements EntityBridge {
    public static final EntityBridgeImpl INSTANCE = new EntityBridgeImpl();

    @Override
    public EntityType<SeatEntity> createSeatType() {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, SeatEntity::new)
            .dimensions(EntityDimensions.fixed(0f, 0f))
            .build();
    }
}

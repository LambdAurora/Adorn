package juuxel.adorn.entity;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;

public final class AdornEntities {
    public static final Registrar<EntityType<?>> ENTITIES = RegistrarFactory.get().create(RegistryKeys.ENTITY_TYPE);

    public static final Registered<EntityType<SeatEntity>> SEAT = ENTITIES.register("seat",
        key -> EntityType.Builder.create(SeatEntity::new, SpawnGroup.MISC)
            .dimensions(0f, 0f)
            .build(key));

    public static void init() {
    }
}

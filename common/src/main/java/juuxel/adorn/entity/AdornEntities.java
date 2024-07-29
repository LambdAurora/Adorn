package juuxel.adorn.entity;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;

public final class AdornEntities {
    public static final Registrar<EntityType<?>> ENTITIES = RegistrarFactory.get().create(RegistryKeys.ENTITY_TYPE);

    public static final Registered<EntityType<SeatEntity>> SEAT =
        ENTITIES.register("seat", PlatformBridges.Companion.getEntities()::createSeatType);

    public static void init() {
    }
}

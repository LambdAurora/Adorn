package juuxel.adorn.lib;

import juuxel.adorn.client.renderer.InvisibleEntityRenderer;
import juuxel.adorn.entity.AdornEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class AdornEntitiesFabric {
    @Environment(EnvType.CLIENT)
    public static void initClient() {
        EntityRendererRegistry.register(AdornEntities.SEAT.get(), InvisibleEntityRenderer::new);
    }
}

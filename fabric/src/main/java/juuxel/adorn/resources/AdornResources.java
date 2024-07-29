package juuxel.adorn.resources;

import juuxel.adorn.client.resources.BookManagerFabric;
import juuxel.adorn.client.resources.ColorManagerFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public final class AdornResources {
    @Environment(EnvType.CLIENT)
    public static void initClient() {
        var resourceManagerHelper = ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES);
        resourceManagerHelper.registerReloadListener(ColorManagerFabric.INSTANCE);
        resourceManagerHelper.registerReloadListener(BookManagerFabric.INSTANCE);
    }
}

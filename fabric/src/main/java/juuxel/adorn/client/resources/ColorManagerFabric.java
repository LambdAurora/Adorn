package juuxel.adorn.client.resources;

import juuxel.adorn.AdornCommon;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;

public final class ColorManagerFabric extends ColorManager implements IdentifiableResourceReloadListener {
    public static final ColorManagerFabric INSTANCE = new ColorManagerFabric();
    private static final Identifier ID = AdornCommon.id("color_manager");

    @Override
    public Identifier getFabricId() {
        return ID;
    }
}

package juuxel.adorn.compat.rei;

import juuxel.adorn.AdornCommon;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;

public class AdornReiServer implements REIServerPlugin {
    public static final CategoryIdentifier<BrewerDisplay> BREWER = CategoryIdentifier.of(AdornCommon.id("brewer"));

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(BREWER, new BrewerDisplay.Serializer());
    }
}

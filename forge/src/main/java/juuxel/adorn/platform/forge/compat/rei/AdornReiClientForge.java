package juuxel.adorn.platform.forge.compat.rei;

import juuxel.adorn.compat.rei.client.AdornReiClient;
import me.shedaniel.rei.forge.REIPluginClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@REIPluginClient
public final class AdornReiClientForge extends AdornReiClient {
}

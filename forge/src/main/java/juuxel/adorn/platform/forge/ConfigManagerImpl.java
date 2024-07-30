package juuxel.adorn.platform.forge;

import juuxel.adorn.config.ConfigManager;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class ConfigManagerImpl extends ConfigManager {
    @Override
    protected Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}

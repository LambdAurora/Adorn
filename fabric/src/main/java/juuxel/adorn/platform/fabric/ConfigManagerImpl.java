package juuxel.adorn.platform.fabric;

import juuxel.adorn.config.ConfigManager;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public final class ConfigManagerImpl extends ConfigManager {
    private final Path configDirectory = FabricLoader.getInstance().getConfigDir();

    @Override
    protected Path getConfigDirectory() {
        return configDirectory;
    }
}

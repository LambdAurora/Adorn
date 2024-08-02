package juuxel.adorn.platform.forge;

import juuxel.adorn.platform.MenuBridge;
import juuxel.adorn.util.Logging;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.menu.Menu;
import net.minecraft.menu.MenuType;
import net.minecraft.menu.NamedMenuFactory;
import net.minecraft.util.math.BlockPos;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public final class MenuBridgeImpl implements MenuBridge {
    public static final MenuBridgeImpl INSTANCE = new MenuBridgeImpl();
    private static final Logger LOGGER = Logging.logger();

    @Override
    public void open(PlayerEntity player, @Nullable NamedMenuFactory factory, BlockPos pos) {
        if (factory == null) {
            LOGGER.warn("[Adorn] Menu factory is null, please report this!", new Throwable("Stacktrace").fillInStackTrace());
            return;
        }

        player.openMenu(factory, pos);
    }

    @Override
    public <M extends Menu> MenuType<M> createType(Factory<M> factory) {
        return IMenuTypeExtension.create(factory::create);
    }
}

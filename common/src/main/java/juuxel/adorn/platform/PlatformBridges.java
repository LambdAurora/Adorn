package juuxel.adorn.platform;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;

@InlineServices
public interface PlatformBridges {
    BlockEntityBridge getBlockEntities();
    BlockFactory getBlockFactory();
    EntityBridge getEntities();
    MenuBridge getMenus();
    NetworkBridge getNetwork();
    ResourceBridge getResources();

    @InlineServices.Getter
    static PlatformBridges get() {
        return Services.load(PlatformBridges.class);
    }
}

package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.ClientPlatformBridges;
import juuxel.adorn.client.FluidRenderingBridge;

public final class ClientPlatformBridgesForge implements ClientPlatformBridges {
    @Override
    public FluidRenderingBridge getFluidRendering() {
        return FluidRenderingBridgeForge.INSTANCE;
    }
}

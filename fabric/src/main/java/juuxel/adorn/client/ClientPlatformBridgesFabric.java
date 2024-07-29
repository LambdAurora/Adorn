package juuxel.adorn.client;

public final class ClientPlatformBridgesFabric implements ClientPlatformBridges {
    @Override
    public FluidRenderingBridge getFluidRendering() {
        return FluidRenderingBridgeFabric.INSTANCE;
    }
}

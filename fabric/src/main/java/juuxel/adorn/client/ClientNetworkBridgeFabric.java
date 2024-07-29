package juuxel.adorn.client;

import juuxel.adorn.lib.AdornNetworking;
import net.minecraft.item.ItemStack;

public final class ClientNetworkBridgeFabric implements ClientNetworkBridge {
    @Override
    public void sendSetTradeStack(int syncId, int slotId, ItemStack stack) {
        AdornNetworking.sendSetTradeStack(syncId, slotId, stack);
    }
}

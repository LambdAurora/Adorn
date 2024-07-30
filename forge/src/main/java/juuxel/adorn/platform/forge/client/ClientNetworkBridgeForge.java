package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.ClientNetworkBridge;
import juuxel.adorn.platform.forge.networking.SetTradeStackC2SMessage;
import net.minecraft.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ClientNetworkBridgeForge implements ClientNetworkBridge {
    @Override
    public void sendSetTradeStack(int syncId, int slotId, ItemStack stack) {
        PacketDistributor.SERVER.noArg().send(new SetTradeStackC2SMessage(syncId, slotId, stack));
    }
}

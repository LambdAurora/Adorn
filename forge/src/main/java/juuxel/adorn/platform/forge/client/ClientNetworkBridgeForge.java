package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.ClientNetworkBridge;
import net.minecraft.network.packet.CustomPayload;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ClientNetworkBridgeForge implements ClientNetworkBridge {
    @Override
    public void sendToServer(CustomPayload payload) {
        PacketDistributor.sendToServer(payload);
    }
}

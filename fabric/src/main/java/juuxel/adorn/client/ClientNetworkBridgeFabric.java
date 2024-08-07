package juuxel.adorn.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;

public final class ClientNetworkBridgeFabric implements ClientNetworkBridge {
    @Override
    public void sendToServer(CustomPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}

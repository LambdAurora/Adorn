package juuxel.adorn.client;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.network.packet.CustomPayload;

@InlineServices
public interface ClientNetworkBridge {
    void sendToServer(CustomPayload payload);

    @InlineServices.Getter
    static ClientNetworkBridge get() {
        return Services.load(ClientNetworkBridge.class);
    }
}

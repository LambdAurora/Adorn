package juuxel.adorn.client;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.item.ItemStack;

@InlineServices
public interface ClientNetworkBridge {
    void sendSetTradeStack(int syncId, int slotId, ItemStack stack);

    @InlineServices.Getter
    static ClientNetworkBridge get() {
        return Services.load(ClientNetworkBridge.class);
    }
}

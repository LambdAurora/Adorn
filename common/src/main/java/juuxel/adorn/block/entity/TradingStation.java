package juuxel.adorn.block.entity;

import juuxel.adorn.trading.Trade;
import juuxel.adorn.util.InventoryComponent;
import net.minecraft.text.Text;

public interface TradingStation {
    Text getOwnerName();
    Trade getTrade();
    InventoryComponent getStorage();

    static TradingStation createEmpty() {
        return new TradingStation() {
            @Override
            public Text getOwnerName() {
                return Text.empty();
            }

            @Override
            public Trade getTrade() {
                return Trade.empty();
            }

            @Override
            public InventoryComponent getStorage() {
                return new InventoryComponent(TradingStationBlockEntity.STORAGE_SIZE);
            }
        };
    }
}

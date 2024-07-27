package juuxel.adorn.trading;

import juuxel.adorn.util.InventoryComponent;

public final class TradeInventory extends InventoryComponent {
    private final Trade trade;

    public TradeInventory(Trade trade) {
        super(2);
        this.trade = trade;

        setStack(0, trade.getSelling());
        setStack(1, trade.getPrice());

        addListener(sender -> {
            trade.setSelling(getStack(0));
            trade.setPrice(getStack(1));
            trade.callListeners();
        });
    }

    public Trade getTrade() {
        return trade;
    }
}

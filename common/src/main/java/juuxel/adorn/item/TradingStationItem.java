package juuxel.adorn.item;

import juuxel.adorn.block.entity.TradingStationBlockEntity;
import juuxel.adorn.trading.Trade;
import juuxel.adorn.util.NbtUtil;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public final class TradingStationItem extends BaseBlockItem {
    public TradingStationItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public boolean canBeNested() {
        // Don't allow putting trading stations inside shulker boxes or other trading stations.
        return false;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        var nbt = stack.getSubNbt(BLOCK_ENTITY_TAG_KEY);
        if (nbt != null) {
            var tradeNbt = NbtUtil.getCompoundOrNull(nbt, TradingStationBlockEntity.NBT_TRADE);
            if (tradeNbt != null) {
                var trade =  Trade.fromNbt(tradeNbt);
                if (!trade.isFullyEmpty()) {
                    return Optional.of(trade);
                }
            }
        }

        return Optional.empty();
    }
}

package juuxel.adorn.loot;

import com.mojang.serialization.Codec;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.entity.TradingStationBlockEntity;
import juuxel.adorn.trading.Trade;
import juuxel.adorn.util.InventoryComponent;
import juuxel.adorn.util.NbtExtensionsKt;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.nbt.NbtCompound;

public final class CheckTradingStationOwnerLootFunction implements LootFunction {
    public static final CheckTradingStationOwnerLootFunction INSTANCE = new CheckTradingStationOwnerLootFunction();
    public static final Codec<CheckTradingStationOwnerLootFunction> CODEC = Codec.unit(INSTANCE);

    private CheckTradingStationOwnerLootFunction() {
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext lootContext) {
        if (stack.isOf(AdornBlocks.TRADING_STATION.get().asItem())) {
            var blockEntityNbt = stack.getSubNbt(BlockItem.BLOCK_ENTITY_TAG_KEY);
            if (blockEntityNbt != null) {
                if (!hasTrade(blockEntityNbt) || !hasStorage(blockEntityNbt)) {
                    clearOwner(blockEntityNbt);
                }
            }
        }

        return stack;
    }

    private boolean hasTrade(NbtCompound nbt) {
        var tradeNbt = NbtExtensionsKt.getCompoundOrNull(nbt, TradingStationBlockEntity.NBT_TRADE);
        if (tradeNbt == null) return false;
        var trade = Trade.fromNbt(tradeNbt);
        return !trade.isFullyEmpty();
    }

    private boolean hasStorage(NbtCompound nbt) {
        var storageNbt = NbtExtensionsKt.getCompoundOrNull(nbt, TradingStationBlockEntity.NBT_STORAGE);
        if (storageNbt == null) return false;
        var inventory = new InventoryComponent(TradingStationBlockEntity.STORAGE_SIZE);
        inventory.readNbt(storageNbt);
        return !inventory.isEmpty();
    }

    private void clearOwner(NbtCompound nbt) {
        nbt.remove(TradingStationBlockEntity.NBT_TRADING_OWNER);
        nbt.remove(TradingStationBlockEntity.NBT_TRADING_OWNER_NAME);
    }

    @Override
    public LootFunctionType getType() {
        return AdornLootFunctionTypes.CHECK_TRADING_STATION_OWNER.get();
    }
}

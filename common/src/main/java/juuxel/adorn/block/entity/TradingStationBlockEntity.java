package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.menu.TradingStationMenu;
import juuxel.adorn.trading.Trade;
import juuxel.adorn.util.AdornUtil;
import juuxel.adorn.util.InventoryComponent;
import juuxel.adorn.util.NbtUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.menu.Menu;
import net.minecraft.menu.NamedMenuFactory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public final class TradingStationBlockEntity extends BlockEntity implements NamedMenuFactory, TradingStation {
    public static final int STORAGE_SIZE = 12;
    public static final String NBT_TRADING_OWNER = "TradingOwner";
    public static final String NBT_TRADING_OWNER_NAME = "TradingOwnerName";
    public static final String NBT_TRADE = "Trade";
    public static final String NBT_STORAGE = "Storage";
    public static final Text UNKNOWN_OWNER = Text.literal("???");

    private @Nullable UUID owner = null;
    private Text ownerName = UNKNOWN_OWNER;
    private final Trade trade = Trade.empty();
    private final InventoryComponent storage = new InventoryComponent(STORAGE_SIZE);

    public TradingStationBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.TRADING_STATION.get(), pos, state);

        trade.addListener(sender -> markDirty());
        storage.addListener(sender -> markDirty());
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }

    public void setOwner(PlayerEntity player) {
        owner = player.getGameProfile().getId();
        ownerName = Text.literal(player.getGameProfile().getName());
        markDirty();
    }

    public boolean isStorageStocked() {
        return storage.getAmountWithNbt(trade.getSelling()) >= trade.getSelling().getCount();
    }

    public boolean isOwner(PlayerEntity player) {
        return player.getGameProfile().getId().equals(owner);
    }

    @Override
    public Menu createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TradingStationMenu(syncId, playerInventory, AdornUtil.menuContextOf(this));
    }

    @Override
    public Text getDisplayName() {
        return getCachedState().getBlock().getName();
    }

    @Override
    public Text getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(Text ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public Trade getTrade() {
        return trade;
    }

    @Override
    public InventoryComponent getStorage() {
        return storage;
    }

    // NBT

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.containsUuid(NBT_TRADING_OWNER)) {
            owner = nbt.getUuid(NBT_TRADING_OWNER);
        }

        ownerName = Objects.requireNonNullElse(NbtUtil.getText(nbt, NBT_TRADING_OWNER_NAME), UNKNOWN_OWNER);
        trade.readNbt(nbt.getCompound(NBT_TRADE));
        storage.readNbt(nbt.getCompound(NBT_STORAGE));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (owner != null) {
            nbt.putUuid(NBT_TRADING_OWNER, owner);
        }

        NbtUtil.putText(nbt, NBT_TRADING_OWNER_NAME, ownerName);

        nbt.put(NBT_TRADE, trade.writeNbt(new NbtCompound()));
        nbt.put(NBT_STORAGE, storage.writeNbt(new NbtCompound()));
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}

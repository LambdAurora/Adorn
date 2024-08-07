package juuxel.adorn.entity;

import juuxel.adorn.block.SeatBlock;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.util.NbtUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class SeatEntity extends Entity {
    private static final TrackedData<BlockPos> SEAT_POS = DataTracker.registerData(SeatEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final String NBT_SEAT_POS = "SeatPos";
    private BlockPos seatPos;

    public SeatEntity(EntityType<?> type, World world) {
        super(type, world);
        noClip = true;
        setInvulnerable(true);
        seatPos = BlockPos.ofFloored(getPos());
    }

    private void setSeatPos(BlockPos seatPos) {
        this.seatPos = seatPos;
        dataTracker.set(SEAT_POS, seatPos);
    }

    public void setPos(BlockPos pos) {
        if (getWorld().isClient) {
            throw new IllegalStateException("setPos must be called on the logical server");
        }
        updatePosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        setSeatPos(pos);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        player.startRiding(this);
        return ActionResult.success(getWorld().isClient);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        kill();
    }

    @Override
    public void kill() {
        removeAllPassengers();
        if (!getWorld().isClient) {
            PlatformBridges.get().getNetwork().sendToTracking(this, new EntityPassengersSetS2CPacket(this));
        }
        super.kill();
        var state = getWorld().getBlockState(seatPos);
        if (state.getBlock() instanceof SeatBlock) {
            getWorld().setBlockState(seatPos, state.with(SeatBlock.OCCUPIED, false));
        }
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(SEAT_POS, BlockPos.ORIGIN);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        seatPos = NbtUtil.getBlockPos(nbt, NBT_SEAT_POS);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        NbtUtil.putBlockPos(nbt, NBT_SEAT_POS, seatPos);
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        var seatPos = dataTracker.get(SEAT_POS);
        var state = getWorld().getBlockState(seatPos);
        var block = state.getBlock();

        // Add the offset that comes from the block's shape
        var blockOffset = block instanceof SeatBlock seat ? seat.getSittingOffset(getWorld(), state, seatPos) : 0.0;
        // Remove the inherent offset that comes from this entity not being directly where the block is
        var posOffset = getY() - seatPos.getY();

        return new Vec3d(0, blockOffset - posOffset, 0);
    }
}

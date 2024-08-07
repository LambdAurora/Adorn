package juuxel.adorn.platform.forge.event;

import juuxel.adorn.block.SofaBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;

public final class EntityEvents {
    public static void init() {
        NeoForge.EVENT_BUS.addListener(EntityEvents::handleSofaSleepTime);
        NeoForge.EVENT_BUS.addListener(EntityEvents::preventSofaSpawns);
    }

    private static void handleSofaSleepTime(CanContinueSleepingEvent event) {
        BlockPos sleepingPos = event.getEntity().getSleepingPosition().orElse(null);
        if (sleepingPos == null) return;
        World world = event.getEntity().getWorld();

        if (event.getProblem() == PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW && world.isDay()
            && world.getBlockState(sleepingPos).getBlock() instanceof SofaBlock) {
            event.setContinueSleeping(true);
        }
    }

    private static void preventSofaSpawns(PlayerSetSpawnEvent event) {
        BlockPos pos = event.getNewSpawn();

        if (pos != null) {
            if (!event.isForced() && event.getEntity().getWorld().getBlockState(pos).getBlock() instanceof SofaBlock) {
                event.setCanceled(true);
            }
        }
    }
}

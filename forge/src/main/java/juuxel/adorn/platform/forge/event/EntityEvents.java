package juuxel.adorn.platform.forge.event;

import juuxel.adorn.block.SofaBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.neoforged.neoforge.event.entity.player.SleepingTimeCheckEvent;

public final class EntityEvents {
    public static void init() {
        NeoForge.EVENT_BUS.addListener(EntityEvents::handleSofaSleepTime);
        NeoForge.EVENT_BUS.addListener(EntityEvents::preventSofaSpawns);
    }

    private static void handleSofaSleepTime(SleepingTimeCheckEvent event) {
        BlockPos sleepingPos = event.getSleepingLocation().orElse(null);
        if (sleepingPos == null) return;
        World world = event.getEntity().getWorld();

        if (world.isDay() && world.getBlockState(sleepingPos).getBlock() instanceof SofaBlock) {
            event.setResult(Event.Result.ALLOW);
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

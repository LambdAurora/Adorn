package juuxel.adorn.compat;

import juuxel.adorn.block.BenchBlock;
import juuxel.adorn.block.ChairBlock;
import juuxel.adorn.block.ChimneyBlock;
import juuxel.adorn.block.CoffeeTableBlock;
import juuxel.adorn.block.CopperPipeBlock;
import juuxel.adorn.block.PicketFenceBlock;
import juuxel.adorn.block.PlatformBlock;
import juuxel.adorn.block.PostBlock;
import juuxel.adorn.block.PrismarineChimneyBlock;
import juuxel.adorn.block.ShelfBlock;
import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.block.StepBlock;
import juuxel.adorn.block.TableBlock;
import juuxel.adorn.block.TableLampBlock;
import juuxel.adorn.block.TradingStationBlock;
import juuxel.adorn.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import virtuoel.statement.api.StateRefresher;
import virtuoel.towelette.api.FluidProperties;
import virtuoel.towelette.api.ToweletteConfig;

public final class ToweletteCompat {
    public static void init() {
        boolean flowing = isFlowingFluidloggingEnabled();

        RegistryUtil.visit(Registries.BLOCK, block -> {
            if (shouldFluidlog(block)) {
                StateRefresher.INSTANCE.addBlockProperty(block, FluidProperties.FLUID, Registries.FLUID.getDefaultId());

                if (flowing) {
                    StateRefresher.INSTANCE.addBlockProperty(block, FluidProperties.LEVEL_1_8, 8);
                    StateRefresher.INSTANCE.addBlockProperty(block, FluidProperties.FALLING, false);
                }
            }
        });
    }

    private static boolean isFlowingFluidloggingEnabled() {
        var data = ToweletteConfig.DATA.get("flowingFluidlogging");
        if (data != null && data.isJsonPrimitive()) {
            return data.getAsBoolean();
        }

        return false;
    }

    private static boolean shouldFluidlog(Block block) {
        return block instanceof BenchBlock ||
            block instanceof ChairBlock ||
            block instanceof ChimneyBlock ||
            block instanceof CoffeeTableBlock ||
            block instanceof CopperPipeBlock ||
            block instanceof PicketFenceBlock ||
            block instanceof PlatformBlock ||
            block instanceof PostBlock ||
            block instanceof PrismarineChimneyBlock ||
            block instanceof ShelfBlock ||
            block instanceof SofaBlock ||
            block instanceof StepBlock ||
            block instanceof TableBlock ||
            block instanceof TableLampBlock ||
            block instanceof TradingStationBlock;
    }
}

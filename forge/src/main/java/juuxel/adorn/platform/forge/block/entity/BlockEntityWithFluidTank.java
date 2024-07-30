package juuxel.adorn.platform.forge.block.entity;

import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface BlockEntityWithFluidTank {
    FluidTank getTank();
}

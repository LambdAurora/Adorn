package juuxel.adorn.client.renderer;

import juuxel.adorn.block.entity.KitchenSinkBlockEntityFabric;
import juuxel.adorn.fluid.FluidUnit;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public final class KitchenSinkRendererFabric extends KitchenSinkRenderer<KitchenSinkBlockEntityFabric> {
    public KitchenSinkRendererFabric(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected double getFluidLevel(KitchenSinkBlockEntityFabric entity) {
        return FluidUnit.convertAsDouble(entity.getStorage().getAmount(), FluidUnit.DROPLET, FluidUnit.LITRE);
    }

    @Override
    protected boolean isEmpty(KitchenSinkBlockEntityFabric entity) {
        return entity.getStorage().getAmount() == 0;
    }
}

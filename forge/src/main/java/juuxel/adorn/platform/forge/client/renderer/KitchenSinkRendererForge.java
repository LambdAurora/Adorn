package juuxel.adorn.platform.forge.client.renderer;

import juuxel.adorn.client.renderer.KitchenSinkRenderer;
import juuxel.adorn.platform.forge.block.entity.KitchenSinkBlockEntityForge;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public final class KitchenSinkRendererForge extends KitchenSinkRenderer<KitchenSinkBlockEntityForge> {
    public KitchenSinkRendererForge(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected double getFluidLevel(KitchenSinkBlockEntityForge entity) {
        return entity.getTank().getFluidAmount();
    }

    @Override
    protected boolean isEmpty(KitchenSinkBlockEntityForge entity) {
        return entity.getTank().isEmpty();
    }
}

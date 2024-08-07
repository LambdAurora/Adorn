package juuxel.adorn.client.renderer;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public final class InvisibleEntityRenderer extends EntityRenderer<Entity> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("missingno");

    public InvisibleEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return TEXTURE;
    }
}

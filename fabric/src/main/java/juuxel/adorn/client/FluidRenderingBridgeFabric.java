package juuxel.adorn.client;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.util.FluidStorageReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class FluidRenderingBridgeFabric implements FluidRenderingBridge {
    public static final FluidRenderingBridgeFabric INSTANCE = new FluidRenderingBridgeFabric();

    @Environment(EnvType.CLIENT)
    @Override
    public @Nullable Sprite getStillSprite(FluidReference volume) {
        return FluidVariantRendering.getSprite(FluidStorageReference.toFluidVariant(volume));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getColor(FluidReference volume, @Nullable BlockRenderView world, @Nullable BlockPos pos) {
        return FluidVariantRendering.getColor(FluidStorageReference.toFluidVariant(volume), world, pos);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean fillsFromTop(FluidReference volume) {
        return FluidVariantAttributes.isLighterThanAir(FluidStorageReference.toFluidVariant(volume));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public List<Text> getTooltip(FluidReference volume, TooltipContext context, @Nullable Integer maxAmountInLitres) {
        var result = FluidVariantRendering.getTooltip(FluidStorageReference.toFluidVariant(volume), context);

        if (maxAmountInLitres != null) {
            result.add(1, volume.getAmountText(maxAmountInLitres, FluidUnit.LITRE));
        } else {
            result.add(1, volume.getAmountText());
        }

        return result;
    }
}

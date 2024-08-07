package juuxel.adorn.client;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@InlineServices
public interface FluidRenderingBridge {
    @Nullable Sprite getStillSprite(FluidReference volume);

    int getColor(FluidReference volume, @Nullable BlockRenderView world, @Nullable BlockPos pos);

    default int getColor(FluidReference volume) {
        return getColor(volume, null, null);
    }

    boolean fillsFromTop(FluidReference volume);

    List<Text> getTooltip(FluidReference volume, TooltipType type, @Nullable Integer maxAmountInLitres);

    @InlineServices.Getter
    static FluidRenderingBridge get() {
        return Services.load(FluidRenderingBridge.class);
    }
}

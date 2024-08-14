package juuxel.adorn.util;

import com.google.common.base.Suppliers;
import juuxel.adorn.lib.registry.Registered;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.menu.MenuContext;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class AdornUtil {
    private static final Logger LOGGER = Logging.logger();

    public static MutableText toTextWithCount(ItemStack stack) {
        return Text.translatable("text.adorn.item_stack_with_count", stack.getCount(), stack.toHoverableText());
    }

    public static AbstractBlock.Settings copySettingsSafely(Block block) {
        var settings = AbstractBlock.Settings.create();
        caughtProperty(block, "mapColor", () -> settings.mapColor(block.getDefaultMapColor()));
        caughtProperty(block, "luminance", () -> {
            int luminance = block.getDefaultState().getLuminance();
            settings.luminance(state -> luminance);
        });
        caughtProperty(block, "hardness", () -> settings.hardness(block.getDefaultState().getHardness(null, null)));
        caughtProperty(block, "resistance", () -> settings.resistance(block.getBlastResistance()));
        caughtProperty(block, "velocityMultiplier", () -> settings.velocityMultiplier(block.getVelocityMultiplier()));
        caughtProperty(block, "jumpVelocityMultiplier", () -> settings.jumpVelocityMultiplier(block.getJumpVelocityMultiplier()));
        caughtProperty(block, "slipperiness", () -> settings.slipperiness(block.getSlipperiness()));
        caughtProperty(block, "soundGroup", () -> settings.sounds(block.getDefaultState().getSoundGroup()));
        return settings;
    }

    private static void caughtProperty(Block block, String name, Runnable fn) {
        try {
            fn.run();
        } catch (Exception e) {
            LOGGER.warn("[Adorn] Could not get block property {} from {}", name, Registries.BLOCK.getId(block), e);
        }
    }

    public static Direction.Axis turnHorizontally(Direction.Axis axis) {
        return switch (axis) {
            case X -> Direction.Axis.Z;
            case Z -> Direction.Axis.X;
            case Y -> Direction.Axis.Y;
        };
    }

    public static <K, V> Registered<Map<K, V>> associateLazily(K[] keys, Function<K, Registered<V>> mapper) {
        List<Registered<V>> values = new ArrayList<>();
        for (K key : keys) {
            values.add(mapper.apply(key));
        }
        var map = Suppliers.memoize(
            () -> IntStream.range(0, keys.length)
                .boxed()
                .collect(Collectors.toMap(
                    index -> keys[index],
                    index -> values.get(index).get(),
                    (a, b) -> a,
                    LinkedHashMap::new
                ))
        );
        return map::get;
    }

    public static MenuContext menuContextOf(BlockEntity blockEntity) {
        return MenuContext.create(blockEntity.getWorld(), blockEntity.getPos());
    }
}

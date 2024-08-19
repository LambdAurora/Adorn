package juuxel.adorn;

import net.minecraft.block.BlockSetType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.Map;

public final class AdornBlockSetTypes {
    public static final Map<DyeColor, BlockSetType> PAINTED_WOODS = Util.make(new EnumMap<>(DyeColor.class), map -> {
        for (DyeColor color : DyeColor.values()) {
            map.put(color, register(color.getName() + "_wood"));
        }
    });

    public static void init() {
    }

    private static BlockSetType register(String id) {
        return BlockSetType.register(new BlockSetType(AdornCommon.NAMESPACE + ':' + id));
    }
}

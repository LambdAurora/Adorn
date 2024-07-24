package juuxel.adorn.lib;

import juuxel.adorn.AdornCommon;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public final class AdornStats {
    public static final Identifier OPEN_BREWER = register("open_brewer", StatFormatter.DEFAULT);
    public static final Identifier OPEN_DRAWER = register("open_drawer", StatFormatter.DEFAULT);
    public static final Identifier OPEN_KITCHEN_CUPBOARD = register("open_kitchen_cupboard", StatFormatter.DEFAULT);
    public static final Identifier INTERACT_WITH_SHELF = register("interact_with_shelf", StatFormatter.DEFAULT);
    public static final Identifier INTERACT_WITH_TABLE_LAMP = register("interact_with_table_lamp", StatFormatter.DEFAULT);
    public static final Identifier INTERACT_WITH_TRADING_STATION = register("interact_with_trading_station", StatFormatter.DEFAULT);
    public static final Identifier DYE_TABLE_LAMP = register("dye_table_lamp", StatFormatter.DEFAULT);
    public static final Identifier DYE_SOFA = register("dye_sofa", StatFormatter.DEFAULT);
    public static final Identifier SIT_ON_CHAIR = register("sit_on_chair", StatFormatter.DEFAULT);
    public static final Identifier SIT_ON_SOFA = register("sit_on_sofa", StatFormatter.DEFAULT);
    public static final Identifier SIT_ON_BENCH = register("sit_on_bench", StatFormatter.DEFAULT);

    private static Identifier register(String id, StatFormatter formatter) {
        return Stats.register(AdornCommon.NAMESPACE + ':' + id, formatter);
    }

    public static void init() {
    }
}

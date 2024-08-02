package juuxel.adorn;

import net.minecraft.util.Identifier;

public final class AdornCommon {
    public static final String NAMESPACE = "adorn";

    public static Identifier id(String path) {
        return new Identifier(NAMESPACE, path);
    }
}

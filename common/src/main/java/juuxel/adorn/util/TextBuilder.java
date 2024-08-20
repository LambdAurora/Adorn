package juuxel.adorn.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

import java.util.ArrayList;
import java.util.List;

public final class TextBuilder {
    private final List<Text> parts = new ArrayList<>();

    public TextBuilder add(Text text) {
        parts.add(text);
        return this;
    }

    public TextBuilder newLine() {
        parts.add(Text.literal("\n"));
        return this;
    }

    public MutableText build() {
        return TextCodecs.combine(parts);
    }
}

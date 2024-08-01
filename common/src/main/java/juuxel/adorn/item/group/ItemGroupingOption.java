package juuxel.adorn.item.group;

import juuxel.adorn.util.Displayable;
import net.minecraft.text.Text;

public enum ItemGroupingOption implements Displayable {
    BY_MATERIAL("by_material"),
    BY_SHAPE("by_shape");

    private final Text displayName;

    ItemGroupingOption(String id) {
        displayName = Text.translatable("gui.adorn.item_grouping_option." + id);
    }

    @Override
    public Text getDisplayName() {
        return displayName;
    }
}

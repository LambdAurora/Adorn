package juuxel.adorn.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemWithDescription extends Item {
    public ItemWithDescription(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(createDescriptionText(getTranslationKey() + ".description"));
    }

    public static Text createDescriptionText(String translationKey) {
        return Text.translatable(translationKey)
            .styled(style -> style.withItalic(true).withColor(Formatting.DARK_GRAY));
    }
}

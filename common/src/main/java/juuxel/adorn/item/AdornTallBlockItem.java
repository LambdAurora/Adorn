package juuxel.adorn.item;

import juuxel.adorn.block.BlockWithDescription;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TallBlockItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class AdornTallBlockItem extends TallBlockItem {
    public AdornTallBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        if (getBlock() instanceof BlockWithDescription withDescription) {
            tooltip.add(ItemWithDescription.createDescriptionText(withDescription.getDescriptionKey()));
        }
    }
}

package juuxel.adorn.item;

import juuxel.adorn.block.BlockWithDescription;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseBlockItem extends BlockItem {
    public BaseBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (getBlock() instanceof BlockWithDescription withDescription) {
            tooltip.add(ItemWithDescription.createDescriptionText(withDescription.getDescriptionKey()));
        }
    }
}

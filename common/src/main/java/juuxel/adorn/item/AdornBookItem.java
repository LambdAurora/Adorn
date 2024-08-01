package juuxel.adorn.item;

import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class AdornBookItem extends Item {
    private final Identifier bookId;

    public AdornBookItem(Identifier bookId, Settings settings) {
        super(settings);
        this.bookId = bookId;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            PlatformBridges.Companion.getNetwork().sendOpenBookPacket(user, bookId);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        var bookManager = PlatformBridges.Companion.getResources().getBookManager();
        if (bookManager.contains(bookId)) {
            tooltip.add(Text.translatable("book.byAuthor", bookManager.get(bookId).author()).formatted(Formatting.GRAY));
        }
    }
}

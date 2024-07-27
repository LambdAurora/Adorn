package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.menu.KitchenCupboardMenu;
import juuxel.adorn.util.ExtensionsKt;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.menu.Menu;
import net.minecraft.util.math.BlockPos;

public final class KitchenCupboardBlockEntity extends SimpleContainerBlockEntity {
    public KitchenCupboardBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.KITCHEN_CUPBOARD.get(), pos, state, 15);
    }

    @Override
    protected Menu createMenu(int syncId, PlayerInventory inv) {
        return new KitchenCupboardMenu(syncId, inv, this, ExtensionsKt.menuContextOf(this));
    }
}

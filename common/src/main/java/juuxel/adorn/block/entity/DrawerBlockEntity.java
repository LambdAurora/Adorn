package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.menu.DrawerMenu;
import juuxel.adorn.util.AdornUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.menu.Menu;
import net.minecraft.util.math.BlockPos;

public final class DrawerBlockEntity extends SimpleContainerBlockEntity {
    public DrawerBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.DRAWER.get(), pos, state, 15);
    }

    @Override
    protected Menu createMenu(int syncId, PlayerInventory inv) {
        return new DrawerMenu(syncId, inv, this, AdornUtil.menuContextOf(this));
    }
}

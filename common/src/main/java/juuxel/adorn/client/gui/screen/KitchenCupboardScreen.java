package juuxel.adorn.client.gui.screen;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.menu.KitchenCupboardMenu;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class KitchenCupboardScreen extends PalettedMenuScreen<KitchenCupboardMenu> {
    private static final Identifier BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/kitchen_cupboard.png");
    private static final Identifier PALETTE_ID = AdornCommon.id("kitchen_cupboard");

    public KitchenCupboardScreen(KitchenCupboardMenu menu, PlayerInventory playerInventory, Text title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return BACKGROUND_TEXTURE;
    }

    @Override
    protected Identifier getPaletteId() {
        return PALETTE_ID;
    }
}

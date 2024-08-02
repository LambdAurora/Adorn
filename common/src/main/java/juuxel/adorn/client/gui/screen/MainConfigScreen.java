package juuxel.adorn.client.gui.screen;

import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.item.group.ItemGroupingOption;
import juuxel.adorn.util.PropertyRef;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.Arrays;

public final class MainConfigScreen extends AbstractConfigScreen {
    private static final int BUTTON_WIDTH = 200;

    public MainConfigScreen(Screen parent) {
        super(Text.translatable("gui.adorn.config.title"), parent);
    }

    @Override
    protected void init() {
        super.init();
        var config = ConfigManager.config();
        int x = (width - BUTTON_WIDTH) / 2;
        addHeading(Text.translatable("gui.adorn.config.visual"), BUTTON_WIDTH);
        addConfigToggle(BUTTON_WIDTH, PropertyRef.ofField(config.client, "showTradingStationTooltips"));
        addConfigButton(BUTTON_WIDTH, PropertyRef.ofField(config.client, "displayedFluidUnit"), Arrays.asList(FluidUnit.values()));
        addHeading(Text.translatable("gui.adorn.config.creative_inventory"), BUTTON_WIDTH);
        addConfigToggle(BUTTON_WIDTH, PropertyRef.ofField(config.client, "showItemsInStandardGroups"));
        addConfigButton(
            BUTTON_WIDTH,
            PropertyRef.ofField(config, "groupItems"),
            Arrays.asList(ItemGroupingOption.values()),
            true
        );
        addHeading(Text.translatable("gui.adorn.config.other"), BUTTON_WIDTH);
        addDrawableChild(
            ButtonWidget.builder(Text.translatable("gui.adorn.config.game_rule_defaults"),
                    widget -> client.setScreen(new GameRuleDefaultsScreen(this)))
                .position(x, nextChildY)
                .size(BUTTON_WIDTH, 20)
                .build()
        );
        addDrawableChild(
            ButtonWidget.builder(ScreenTexts.DONE, widget -> close())
                .position(this.width / 2 - 100, this.height - 27)
                .size(200, 20)
                .build()
        );
    }
}

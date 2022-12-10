package juuxel.adorn.client.gui.screen

import juuxel.adorn.config.ConfigManager
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.item.group.ItemGroupingOption
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class MainConfigScreen(parent: Screen) : AbstractConfigScreen(Text.translatable("gui.adorn.config.title"), parent) {
    override fun init() {
        super.init()
        val config = ConfigManager.config()
        val x = (width - BUTTON_WIDTH) / 2
        addDrawableChild(createConfigToggle(x, 40, BUTTON_WIDTH, config.client::showTradingStationTooltips))
        addDrawableChild(createConfigToggle(x, 40 + BUTTON_SPACING, BUTTON_WIDTH, config.client::showItemsInStandardGroups, restartRequired = true))
        addDrawableChild(
            createConfigButton(
                x, 40 + 2 * BUTTON_SPACING, BUTTON_WIDTH,
                config::groupItems,
                ItemGroupingOption.values().toList(),
                restartRequired = true
            )
        )
        addDrawableChild(
            ButtonWidget.builder(Text.translatable("gui.adorn.config.game_rule_defaults")) {
                client!!.setScreen(GameRuleDefaultsScreen(this))
            }
                .position(x, 40 + 3 * BUTTON_SPACING)
                .size(BUTTON_WIDTH, 20)
                .build()
        )
        addDrawableChild(createConfigButton(x, 40 + 4 * BUTTON_SPACING, BUTTON_WIDTH, config.client::displayedFluidUnit, FluidUnit.values().toList()))
        addDrawableChild(
            ButtonWidget.builder(ScreenTexts.DONE) { close() }
                .position(this.width / 2 - 100, this.height - 27)
                .size(200, 20)
                .build()
        )
    }

    companion object {
        private const val BUTTON_WIDTH = 200
    }
}

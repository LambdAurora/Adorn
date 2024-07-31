package juuxel.adorn.client.gui.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Narratable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;

/**
 * A wrapper for a widget (obtained by calling {@link #current()}).
 *
 * <p>Used for enhancing the widget with some functionality,
 * such as {@linkplain FlipBook pagination}, {@linkplain ScissorEnvelope scissoring} or
 * {@linkplain ScrollEnvelope scrolling}.
 */
public abstract class WidgetEnvelope implements Element, Drawable, Selectable, TickingElement, Draggable {
    protected abstract Element current();

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (current() instanceof Drawable drawable) {
            drawable.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        current().mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return current().mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return current().mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return current().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return current().mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return current().keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return current().keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return current().charTyped(chr, modifiers);
    }

    @Override
    public boolean isFocused() {
        return current().isFocused();
    }

    @Override
    public void setFocused(boolean focused) {
        current().setFocused(focused);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return current().isMouseOver(mouseX, mouseY);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        if (current() instanceof Narratable narratable) {
            narratable.appendNarrations(builder);
        }
    }

    @Override
    public SelectionType getType() {
        return current() instanceof Selectable selectable ? selectable.getType() : SelectionType.NONE;
    }

    @Override
    public void tick() {
        if (current() instanceof TickingElement ticking) {
            ticking.tick();
        }
    }

    @Override
    public void stopDragging() {
        if (current() instanceof Draggable draggable) {
            draggable.stopDragging();
        }
    }
}

package juuxel.adorn.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A global GL scissor stack that is applied when pushed and popped.
 */
public final class Scissors {
    private static final Deque<Frame> STACK = new ArrayDeque<>();

    /**
     * Pushes a new scissor frame at {@code (x, y)} with dimensions {@code (width, height)}
     * and refreshes the scissor state.
     */
    public static void push(int x, int y, int width, int height) {
        push(new Frame(x, y, x + width, y + height));
    }

    /**
     * Pushes a scissor frame and refreshes the scissor state.
     */
    public static void push(Frame frame) {
        STACK.addLast(frame);
        apply();
    }

    /**
     * Pops the topmost scissor frame and refreshes the scissor state.
     * If there are no remaining frames, disables scissoring.
     */
    public static Frame pop() {
        var frame = STACK.removeLast();
        apply();
        return frame;
    }

    /**
     * Temporarily disables the topmost scissor frame for executing the runnable.
     */
    public static void suspendScissors(Runnable fn) {
        var frame = pop();
        fn.run();
        push(frame);
    }

    private static void apply() {
        if (STACK.isEmpty()) {
            RenderSystem.disableScissor();
            return;
        }

        var window = MinecraftClient.getInstance().getWindow();
        var x1 = 0;
        var y1 = 0;
        var x2 = window.getScaledWidth();
        var y2 = window.getScaledHeight();

        for (var frame : STACK) {
            x1 = Math.max(x1, frame.x1);
            y1 = Math.max(y1, frame.y1);
            x2 = Math.min(x2, frame.x2);
            y2 = Math.min(y2, frame.y2);
        }

        var scale = window.getScaleFactor();
        RenderSystem.enableScissor(
            (int) (x1 * scale), (int) (window.getFramebufferHeight() - scale * y2),
            (int) ((x2 - x1) * scale), (int) ((y2 - y1) * scale)
        );
    }

    public record Frame(int x1, int y1, int x2, int y2) {
    }
}

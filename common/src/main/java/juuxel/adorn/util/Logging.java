package juuxel.adorn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Logging {
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * Gets a logger for the calling class.
     *
     * <p>If called from a nested class, returns a logger
     * for the outermost class in the nest.
     */
    public static Logger logger() {
        var caller = STACK_WALKER.getCallerClass();

        // Locate the outermost class.
        var next = caller.getEnclosingClass();
        while (next != null) {
            caller = next;
            next = caller.getEnclosingClass();
        }

        return LoggerFactory.getLogger(caller);
    }
}

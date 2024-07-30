package juuxel.adorn.platform.forge;

import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.client.resources.ColorManager;
import juuxel.adorn.platform.ResourceBridge;

public final class ResourceBridgeImpl implements ResourceBridge {
    public static final ResourceBridgeImpl INSTANCE = new ResourceBridgeImpl();
    private final BookManager bookManager = new BookManager();
    private final ColorManager colorManager = new ColorManager();

    @Override
    public BookManager getBookManager() {
        return bookManager;
    }

    @Override
    public ColorManager getColorManager() {
        return colorManager;
    }
}

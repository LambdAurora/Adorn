package juuxel.adorn.platform.fabric;

import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.client.resources.BookManagerFabric;
import juuxel.adorn.client.resources.ColorManager;
import juuxel.adorn.client.resources.ColorManagerFabric;
import juuxel.adorn.platform.ResourceBridge;

public final class ResourceBridgeImpl implements ResourceBridge {
    public static final ResourceBridgeImpl INSTANCE = new ResourceBridgeImpl();

    @Override
    public BookManager getBookManager() {
        return BookManagerFabric.INSTANCE;
    }

    @Override
    public ColorManager getColorManager() {
        return ColorManagerFabric.INSTANCE;
    }
}

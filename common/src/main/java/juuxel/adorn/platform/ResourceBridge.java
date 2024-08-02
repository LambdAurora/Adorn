package juuxel.adorn.platform;

import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.client.resources.ColorManager;

public interface ResourceBridge {
    BookManager getBookManager();
    ColorManager getColorManager();
}

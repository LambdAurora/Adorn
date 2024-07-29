package juuxel.adorn.client.resources;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.client.book.BookManager;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;

public final class BookManagerFabric extends BookManager implements IdentifiableResourceReloadListener {
    public static final BookManagerFabric INSTANCE = new BookManagerFabric();
    private static final Identifier ID = AdornCommon.id("book_manager");

    @Override
    public Identifier getFabricId() {
        return ID;
    }
}

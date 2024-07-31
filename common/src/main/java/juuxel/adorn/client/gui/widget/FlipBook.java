package juuxel.adorn.client.gui.widget;

import net.minecraft.client.gui.Element;

import java.util.ArrayList;
import java.util.List;

public final class FlipBook extends WidgetEnvelope implements PageContainer<Element> {
    private final List<Element> pages = new ArrayList<>();
    private final Runnable pageUpdateListener;
    private int currentPage = 0;

    public FlipBook(Runnable pageUpdateListener) {
        this.pageUpdateListener = pageUpdateListener;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        pageUpdateListener.run();
    }

    @Override
    public int getPageCount() {
        return pages.size();
    }

    @Override
    public Element getCurrentPageValue() {
        return current();
    }

    @Override
    protected Element current() {
        return pages.get(currentPage);
    }

    public void add(Element page) {
        pages.add(page);
    }

    @Override
    public void showPreviousPage() {
        if (hasPreviousPage()) {
            currentPage--;
            pageUpdateListener.run();
        }
    }

    @Override
    public void showNextPage() {
        if (hasPreviousPage()) {
            currentPage++;
            pageUpdateListener.run();
        }
    }
}

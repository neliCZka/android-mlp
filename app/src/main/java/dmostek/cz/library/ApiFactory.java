package dmostek.cz.library;

import dmostek.cz.library.libraryapi.AvailableBooksListing;
import dmostek.cz.library.libraryapi.BookDetailApi;
import dmostek.cz.library.libraryapi.ImageDownloader;
import dmostek.cz.library.libraryapi.NewsApi;
import dmostek.cz.library.libraryapi.SearchApi;

/**
 * Implement to return concrete implementation of the API.
 */
public interface ApiFactory {

    public BookDetailApi getBookDetailApi();

    public SearchApi getSearchApi();

    public ImageDownloader getImageDownloader();

    public AvailableBooksListing getAvailableListing();

    public NewsApi getNewsApi();
}

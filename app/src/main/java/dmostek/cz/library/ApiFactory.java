package dmostek.cz.library;

import dmostek.cz.library.libraryapi.BookDetailApi;
import dmostek.cz.library.libraryapi.ImageDownloader;
import dmostek.cz.library.libraryapi.SearchApi;

/**
 * Created by mostek on 5.2.2015.
 */
public interface ApiFactory {

    public BookDetailApi getBookDetailApi();

    public SearchApi getSearchApi();

    public ImageDownloader getImageDownloader();
}

package dmostek.cz.library;

import dmostek.cz.library.libraryapi.BookDetailApi;
import dmostek.cz.library.libraryapi.HtmlBookDetailApi;
import dmostek.cz.library.libraryapi.HtmlImageDownloader;
import dmostek.cz.library.libraryapi.HtmlSearchApi;
import dmostek.cz.library.libraryapi.ImageDownloader;
import dmostek.cz.library.libraryapi.SearchApi;

/**
 * Created by mostek on 5.2.2015.
 */
public class HtmlApiFactory implements ApiFactory {

    @Override
    public BookDetailApi getBookDetailApi() {
        return new HtmlBookDetailApi();
    }

    @Override
    public SearchApi getSearchApi() {
        return new HtmlSearchApi();
    }

    @Override
    public ImageDownloader getImageDownloader() {
        return new HtmlImageDownloader();
    }
}

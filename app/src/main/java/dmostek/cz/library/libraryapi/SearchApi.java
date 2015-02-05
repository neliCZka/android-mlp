package dmostek.cz.library.libraryapi;

import dmostek.cz.library.BookThumbnail;
import rx.Observable;

/**
 * Created by mostek on 30.1.2015.
 */
public interface SearchApi {

    /**
     *
     * @param url
     * @return
     */
    // TODO add parameters
    public Observable<BookThumbnail> search(String url);
}

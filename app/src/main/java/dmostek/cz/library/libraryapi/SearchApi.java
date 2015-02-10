package dmostek.cz.library.libraryapi;

import rx.Observable;

/**
 * API providing interface to the book search engine.
 */
public interface SearchApi {

    public Observable<BookSearchItem> search(String url);

    public Observable<BookSearchItem> search(String url, int page);

}

package dmostek.cz.library.libraryapi;

import rx.Observable;

/**
 * API that provides {@link dmostek.cz.library.libraryapi.BookDetail detail} of the book.
 */
public interface BookDetailApi {

    public Observable<BookDetail> getBookDetail(String id);
}

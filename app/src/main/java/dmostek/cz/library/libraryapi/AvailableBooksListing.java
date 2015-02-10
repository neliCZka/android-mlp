package dmostek.cz.library.libraryapi;

import rx.Observable;

/**
 * Created by mostek on 9.2.2015.
 */
public interface AvailableBooksListing {

    public Observable<BookToBorrow> getAvailableBooks(String bookId);
}

package dmostek.cz.library.libraryapi;

import rx.Observable;

/**
 * Created by mostek on 3.2.2015.
 */
public interface BookDetailApi {

    public Observable<BookDetail> getBookDetail(String id);
}

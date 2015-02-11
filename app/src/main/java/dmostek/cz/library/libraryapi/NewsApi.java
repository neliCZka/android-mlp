package dmostek.cz.library.libraryapi;

import rx.Observable;

/**
 * Created by mostek on 11.2.2015.
 */
public interface NewsApi {

    public Observable<NewThumbnail> getNews();
}

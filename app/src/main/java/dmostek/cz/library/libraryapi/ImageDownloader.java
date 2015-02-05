package dmostek.cz.library.libraryapi;

import android.graphics.Bitmap;

import rx.Observable;

/**
 * Created by mostek on 5.2.2015.
 */
public interface ImageDownloader {

    public Observable<Bitmap> loadBookThumbnail(String id);

}

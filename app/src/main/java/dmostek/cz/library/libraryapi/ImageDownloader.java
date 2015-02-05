package dmostek.cz.library.libraryapi;

import android.graphics.Bitmap;

import java.net.URL;

import rx.Observable;

/**
 * API which loads image from given URL.
 */
public interface ImageDownloader {

    public Observable<Bitmap> loadBookThumbnail(URL url);

}

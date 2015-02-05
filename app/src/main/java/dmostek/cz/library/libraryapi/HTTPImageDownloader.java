package dmostek.cz.library.libraryapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;

/**
 * Image downloader which downloads image thumbnail over HTTP.
 */
public class HTTPImageDownloader implements ImageDownloader {

    @Override
    public Observable<Bitmap> loadBookThumbnail(final URL url) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(HTMLApi.REQUEST_TIMEOUT);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    subscriber.onNext(BitmapFactory.decodeStream(input));
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}

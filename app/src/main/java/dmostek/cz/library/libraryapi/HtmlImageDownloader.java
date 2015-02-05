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
 * Created by mostek on 5.2.2015.
 */
public class HtmlImageDownloader implements ImageDownloader {

    @Override
    public Observable<Bitmap> loadBookThumbnail(final String url) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
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

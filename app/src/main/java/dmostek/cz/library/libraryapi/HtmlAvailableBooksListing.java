package dmostek.cz.library.libraryapi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by mostek on 9.2.2015.
 */
public class HtmlAvailableBooksListing implements AvailableBooksListing {
    @Override
    public Observable<BookToBorrow> getAvailableBooks(final String bookId) {
        // TODO
        // http://msearch.mlp.cz/cz/vk/test/2844441/
        return Observable.create(new Observable.OnSubscribe<BookToBorrow>() {
            @Override
            public void call(Subscriber<? super BookToBorrow> subscriber) {
                Document document = null;
                try {
                    document = Jsoup.connect("http://msearch.mlp.cz/cz/vk/test/" + bookId + "/")
                        .timeout(HTMLApi.REQUEST_TIMEOUT)
                        .get();

                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}

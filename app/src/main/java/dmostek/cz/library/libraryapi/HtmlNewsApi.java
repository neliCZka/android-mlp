package dmostek.cz.library.libraryapi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by mostek on 11.2.2015.
 */
public class HtmlNewsApi implements NewsApi {
    @Override
    public Observable<NewThumbnail> getNews() {
        // http://m.mlp.cz/cz/novinky/
        return Observable.create(new Observable.OnSubscribe<NewThumbnail>() {
            @Override
            public void call(Subscriber<? super NewThumbnail> subscriber) {
                Document document = null;
                try {
                    document = Jsoup.connect("http://m.mlp.cz/cz/novinky/")
                            .timeout(HTMLApi.REQUEST_TIMEOUT)
                            .get();
                    NewThumbnail newThumbnail = new NewThumbnail();
                    Elements news = document.select(".art.clearfix");
                    for (Element aNew : news) {
                        String text = aNew.select("h2 > a").get(0).text();
                        newThumbnail.setTitle(text);
                    }
                    subscriber.onNext(newThumbnail);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}

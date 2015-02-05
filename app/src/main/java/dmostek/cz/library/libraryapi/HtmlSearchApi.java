package dmostek.cz.library.libraryapi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

import dmostek.cz.library.BookThumbnail;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by mostek on 30.1.2015.
 */
public class HtmlSearchApi implements SearchApi {

    @Override
    public Observable<BookThumbnail> search(final String term) {
        return Observable.create(new Observable.OnSubscribe<BookThumbnail>() {
            @Override
            public void call(Subscriber<? super BookThumbnail> subscriber) {
                try {
                    Document document = Jsoup.connect("http://msearch.mlp.cz/cz/?&query=" + URLEncoder.encode(term.trim(), "utf-8") + "&kde=t-o-v-d&action=sOnlineKatalog&navigation=%2Bngeneric4%3A%5E%22kni%22%24n%24")
                            .timeout(HTMLApi.REQUEST_TIMEOUT)
                            .get();
                    Elements select = document.select("div.item");
                    for (Element element : select) {
                        Element title = element.select("h3").get(0);
                        Element link = element.select("button").get(0);
                        Elements imgElemtens = element.select("div.cover img");
                        final BookThumbnail bookThumbnail = new BookThumbnail();
                        if (imgElemtens != null && !imgElemtens.isEmpty()) {
                            Element imgElement = imgElemtens.get(0);
                            final String src = imgElement.attr("src");
                            bookThumbnail.setThumbnailId(src);
                        }
                        bookThumbnail.setName(title.childNode(0).toString());
                        String rel = link.attr("rel");
                        String[] split = rel.split("/");
                        bookThumbnail.setId(split[split.length - 1]);
                        subscriber.onNext(bookThumbnail);
                    }
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}

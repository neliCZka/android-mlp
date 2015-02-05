package dmostek.cz.library.libraryapi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import rx.Observable;
import rx.Subscriber;

/**
 * Implementation of the search API that parses search results from the HTML page.
 */
public class HtmlSearchApi implements SearchApi {

    @Override
    public Observable<BookSearchItem> search(final String term) {
        return Observable.create(new Observable.OnSubscribe<BookSearchItem>() {
            @Override
            public void call(Subscriber<? super BookSearchItem> subscriber) {
                try {
                    Document document = Jsoup.connect("http://msearch.mlp.cz/cz/?&query=" + URLEncoder.encode(term.trim(), "utf-8") + "&kde=t-o-v-d&action=sOnlineKatalog&navigation=%2Bngeneric4%3A%5E%22kni%22%24n%24")
                            .timeout(HTMLApi.REQUEST_TIMEOUT)
                            .get();
                    Elements select = document.select("div.item");
                    for (Element element : select) {
                        Element title = element.select("h3").get(0);
                        Element link = element.select("button").get(0);
                        Elements imgElemtens = element.select("div.cover img");
                        final BookSearchItem bookSearchItem = new BookSearchItem();
                        if (imgElemtens != null && !imgElemtens.isEmpty()) {
                            Element imgElement = imgElemtens.get(0);
                            final String src = imgElement.attr("src");
                            bookSearchItem.setThumbnailUrl(new URL(src));
                        }
                        bookSearchItem.setName(title.childNode(0).toString());
                        String rel = link.attr("rel");
                        String[] split = rel.split("/");
                        bookSearchItem.setId(split[split.length - 1]);
                        subscriber.onNext(bookSearchItem);
                    }
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}

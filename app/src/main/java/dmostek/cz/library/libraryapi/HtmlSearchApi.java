package dmostek.cz.library.libraryapi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
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
                    Document document = getDocument(term, 0);
                    processDocument(subscriber, document);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<BookSearchItem> search(final String term, final int page) {
        return Observable.create(new Observable.OnSubscribe<BookSearchItem>() {
            @Override
            public void call(Subscriber<? super BookSearchItem> subscriber) {
                try {
                    Document document = getDocument(term, page);
                    processDocument(subscriber, document);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private void processDocument(Subscriber<? super BookSearchItem> subscriber, Document document) throws MalformedURLException {
        Elements select = document.select("div.item");
        for (Element element : select) {
            Element title = element.select("h3").get(0);
            Elements buttonElements = element.select("button");
            if (buttonElements.isEmpty()) {
                continue;
            }
            Element link = buttonElements.get(0);
            Elements imgElements = element.select("div.cover img");
            final BookSearchItem bookSearchItem = new BookSearchItem();
            if (imgElements != null && !imgElements.isEmpty()) {
                Element imgElement = imgElements.get(0);
                final String src = imgElement.attr("src");
                bookSearchItem.setThumbnailUrl(new URL(src));
            }
            Elements tags = element.select("span.tag.red");
            bookSearchItem.setType(SearchItemType.BOOK);
            for (Element tag : tags) {
                if (tag.text().equals("Tituly")) {
                    bookSearchItem.setType(SearchItemType.TITLE);
                    break;
                }
            }
            bookSearchItem.setName(title.childNode(0).toString());
            String rel = link.attr("rel");
            String[] split = rel.split("/");
            bookSearchItem.setId(split[split.length - 1]);
            subscriber.onNext(bookSearchItem);
        }
        subscriber.onCompleted();
    }


    private Document getDocument(String term, int page) throws IOException {
        return Jsoup.connect("http://msearch.mlp.cz/cz/?&query=" + URLEncoder.encode(term.trim(), "utf-8") + "&kde=t-o-v-d&action=sOnlineKatalog&navigation=%2Bngeneric4%3A%5E%22kni%22%24n%24&offset=" + (page * 10))
                .timeout(HTMLApi.REQUEST_TIMEOUT)
                .get();
    }
}

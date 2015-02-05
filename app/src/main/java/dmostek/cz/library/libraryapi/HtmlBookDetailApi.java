package dmostek.cz.library.libraryapi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Implementation of the detail api which parses the HTML page with the book detail.
 */
public class HtmlBookDetailApi implements BookDetailApi {

    @Override
    public Observable<BookDetail> getBookDetail(final String id) {
        return Observable.create(new Observable.OnSubscribe<BookDetail>() {
            @Override
            public void call(Subscriber<? super BookDetail> subscriber) {
                Document document = null;
                try {
                    document = Jsoup.connect("http://msearch.mlp.cz/cz/vyjadreni//" + id + "/")
                            .timeout(HTMLApi.REQUEST_TIMEOUT)
                            .get();
                    Element titleElement = document.select("#katalog_page > div.logged > h1").get(0);
                    Element authorElement = document.select("#anotace-in > div.bhleft > p > a > strong").get(0);
                    Element descriptionElement = document.select("#tabobsah > p").get(0);
                    BookDetail bookDetail = new BookDetail();
                    bookDetail.setTitle(titleElement.text());
                    bookDetail.setAuthor(authorElement.text());
                    bookDetail.setDescription(descriptionElement.text());
                    subscriber.onNext(bookDetail);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}

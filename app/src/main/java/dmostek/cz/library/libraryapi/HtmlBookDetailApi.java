package dmostek.cz.library.libraryapi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by mostek on 3.2.2015.
 */
public class HtmlBookDetailApi implements BookDetailApi {
    @Override
    public Observable<BookDetail> getBookDetail(final String id) {
        return Observable.create(new Observable.OnSubscribe<BookDetail>() {
            @Override
            public void call(Subscriber<? super BookDetail> subscriber) {
                // http://msearch.mlp.cz/cz/vypujcka/2844441/
                Document document = null;
                try {
                    document = Jsoup.connect("http://msearch.mlp.cz/cz/vyjadreni//" + id + "/").timeout(15*1000).get();
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

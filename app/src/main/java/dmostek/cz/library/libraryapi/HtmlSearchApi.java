package dmostek.cz.library.libraryapi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        new Observable.OnSubscribe<BookThumbnail>() {
            @Override
            public void call(Subscriber<? super BookThumbnail> subscriber) {
                try {
//                    isRunning = true;
                    Document document = Jsoup.connect("http://msearch.mlp.cz/cz/?&query=" + URLEncoder.encode(term, "utf-8") + "&kde=t-o-v-d&action=sOnlineKatalog&navigation=%2Bngeneric4%3A%5E%22kni%22%24n%24  ").get();
                    Elements select = document.select("div.item");
                    for (Element element : select) {
                        Element title = element.select("h3").get(0);
                        Elements imgElemtens = element.select("div.cover img");
                        final BookThumbnail bookThumbnail = new BookThumbnail();
                        if (imgElemtens != null && !imgElemtens.isEmpty()) {
                            Element imgElement = imgElemtens.get(0);
                            final String src = imgElement.attr("src");
//                            photoDownloader(src)
//                                    .subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribe(new Action1<Bitmap>() {
//                                        @Override
//                                        public void call(Bitmap o) {
//                                            bookThumbnail.setThumbnail(new BitmapDrawable(context.getResources(), o));
//                                            adapter.itemChanged(bookThumbnail);
//                                        }
//                                    });
                        }
                        bookThumbnail.setName(title.childNode(0).toString());
//                        bookThumbnail.setThumbnail(context.getResources().getDrawable(R.drawable.no_book_thumbnail));
                        subscriber.onNext(bookThumbnail);
                    }
                    subscriber.onCompleted();
                } catch (Exception e){
//                    isRunning = false;
                    subscriber.onError(e);
                }
            }
        };
        return null;
    }
}

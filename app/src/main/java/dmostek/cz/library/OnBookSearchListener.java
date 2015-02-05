package dmostek.cz.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by mostek on 28.1.2015.
 */
public class OnBookSearchListener implements View.OnClickListener {

    private ProgressWheel progressBar;
    private EditText searchInput;
    private final BookSearchResultAdapter adapter;
    private final Context context;
    private View view;
    private boolean isRunning = false;

    public OnBookSearchListener(BookSearchResultAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    public void setView(View view) {
        this.view = view;
        this.searchInput = (EditText) view.findViewById(R.id.book_search);
        this.progressBar = (ProgressWheel) view.findViewById(R.id.progress_wheel);
        if (isRunning) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        String term = searchInput.getText().toString();
        if (term == null) {
            return;
        }
        term = term.trim();
        if (term.isEmpty()) {
            return;
        }
        adapter.clear();
        adapter.notifyDataSetChanged();
        progressBar.spin();
        progressBar.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Observable<BookThumbnail> bookThumbnailObservable = Observable.create(getUrl(term));
        bookThumbnailObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookThumbnail>() {
                               @Override
                               public void onCompleted() {
                                   progressBar.stopSpinning();
                                   isRunning = false;
                                   progressBar.setVisibility(View.GONE);
                                   adapter.notifyDataSetChanged();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   isRunning = false;
                                   progressBar.setVisibility(View.GONE);
                                   // TODO make a toast
                                   System.out.println(e.getMessage());
                               }

                               @Override
                               public void onNext(BookThumbnail s) {
                                   adapter.add(s);
                               }
                           }

                );
    }


    private Observable.OnSubscribe<BookThumbnail> getUrl(final String searchTerm) {
        isRunning = true;
        return new Observable.OnSubscribe<BookThumbnail>() {
            @Override
            public void call(Subscriber<? super BookThumbnail> subscriber) {
                try {
                    isRunning = true;
                    Document document = Jsoup.connect("http://msearch.mlp.cz/cz/?&query=" + URLEncoder.encode(searchTerm, "utf-8") + "&kde=t-o-v-d&action=sOnlineKatalog&navigation=%2Bngeneric4%3A%5E%22kni%22%24n%24  ")
                            .timeout(10000)
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
                            photoDownloader(src)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Bitmap>() {
                                        @Override
                                        public void call(Bitmap o) {
                                            bookThumbnail.setThumbnail(new BitmapDrawable(context.getResources(), o));
                                            adapter.itemChanged(bookThumbnail);
                                        }
                                    });
                        }
                        bookThumbnail.setName(title.childNode(0).toString());
                        String rel = link.attr("rel");
                        String[] split = rel.split("/");
                        bookThumbnail.setId(split[split.length - 1]);
                        bookThumbnail.setThumbnail(context.getResources().getDrawable(R.drawable.no_book_thumbnail));
                        subscriber.onNext(bookThumbnail);
                    }
                    subscriber.onCompleted();
                } catch (Exception e){
                    isRunning = false;
                    subscriber.onError(e);
                }
            }
        };
    }

    private Observable<Bitmap> photoDownloader(final String url) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber subscriber) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
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

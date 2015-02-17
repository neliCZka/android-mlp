package dmostek.cz.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.net.URL;

import dmostek.cz.library.libraryapi.BookSearchItem;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Listens onn event when the book search is triggered by the user.
 */
public class OnBookSearchListener implements View.OnClickListener, PagingListener {

    private ProgressWheel progressBar;
    private EditText searchInput;
    private final BookSearchResultAdapter adapter;
    private final Context context;
    private View view;
    private ErrorView errorView;
    private RecyclerView listView;
    private ErrorType lastErrorStatus;
    private int page = 0;
    private String term;
    private Subscription subscription = new NullSubscription();

    public OnBookSearchListener(BookSearchResultAdapter adapter, Context context) {
        this.adapter = adapter;
        adapter.setOnPagingListener(this);
        this.context = context;
    }

    public void setView(final View view) {
        this.view = view;
        this.searchInput = (EditText) view.findViewById(R.id.book_search);
        this.progressBar = (ProgressWheel) view.findViewById(R.id.progress_wheel);
        this.listView = (RecyclerView) view.findViewById(R.id.search_list);
        this.errorView = (ErrorView) view.findViewById(R.id.error_view);
        this.errorView.setListener(new OnRetryListener() {
            @Override
            public void onRetry() {
                OnBookSearchListener.this.onClick(view);
            }
        });
        if (!subscription.isUnsubscribed()) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (lastErrorStatus != null) {
            errorView.setVisibility(View.VISIBLE);
        }
        if (adapter.getItemCount() > 0) {
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        this.term = searchInput.getText().toString();
        if (term == null) {
            return;
        }
        term = term.trim();
        if (term.isEmpty()) {
            return;
        }
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        page = 0;
        adapter.clear();
        adapter.notifyDataSetChanged();
        load();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onNextPage() {
        page++;
        load();
    }

    private void load() {
        progressBar.spin();
        progressBar.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        hideKeyboard();
        lastErrorStatus = null;
        subscription = ApplicationUtils.getApiFactory()
                .getSearchApi()
                .search(term, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BookSearchSubscriber(adapter));
    }

    private class ThumbnailSubscriber extends Subscriber<Bitmap> {
        private final BookSearchItem thumbnail;
        private final BookSearchResultAdapter adapter;

        public ThumbnailSubscriber(BookSearchItem thumbnail, BookSearchResultAdapter adapter) {
            this.thumbnail = thumbnail;
            this.adapter = adapter;
        }

        @Override
        public void onCompleted() {
            unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            unsubscribe();
        }

        @Override
        public void onNext(Bitmap bitmap) {
            thumbnail.setThumbnail(new BitmapDrawable(context.getResources(), bitmap));
            adapter.itemChanged(thumbnail);
        }
    }

    private class BookSearchSubscriber extends Subscriber<BookSearchItem> {

        private final BookSearchResultAdapter adapter;

        public BookSearchSubscriber(BookSearchResultAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onCompleted() {
            progressBar.stopSpinning();
//            isRunning = false;
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
//            isRunning = false;
            unsubscribe();
            lastErrorStatus = ExceptionTranslator.translate(e);
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNext(final BookSearchItem bookSearchItem) {
            bookSearchItem.setThumbnail(context.getResources().getDrawable(R.drawable.no_book_thumbnail));
            URL thumbnailUrl = bookSearchItem.getThumbnailUrl();
            ApplicationUtils.getApiFactory()
                    .getImageDownloader()
                    .loadBookThumbnail(thumbnailUrl)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ThumbnailSubscriber(bookSearchItem, adapter));
            adapter.add(bookSearchItem);
        }
    }
}

package dmostek.cz.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.pnikosis.materialishprogress.ProgressWheel;

import dmostek.cz.library.libraryapi.HtmlImageDownloader;
import dmostek.cz.library.libraryapi.HtmlSearchApi;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
        hideKeyboard();
        new HtmlSearchApi()
                .search(term)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BookSearchSubscriber(adapter));
    }

    // FODO move to some util class
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class ThumbnailSubscriber extends Subscriber<Bitmap> {
        private final BookThumbnail thumbnail;
        private final BookSearchResultAdapter adapter;

        public ThumbnailSubscriber(BookThumbnail thumbnail, BookSearchResultAdapter adapter) {
            this.thumbnail = thumbnail;
            this.adapter = adapter;
        }

        @Override
        public void onCompleted() {
            //TODO
        }

        @Override
        public void onError(Throwable e) {
            // TODO
        }

        @Override
        public void onNext(Bitmap bitmap) {
            thumbnail.setThumbnail(new BitmapDrawable(context.getResources(), bitmap));
            adapter.itemChanged(thumbnail);
        }
    }

    private class BookSearchSubscriber extends Subscriber<BookThumbnail> {

        private final BookSearchResultAdapter adapter;

        public BookSearchSubscriber(BookSearchResultAdapter adapter) {
            this.adapter = adapter;
        }

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
        public void onNext(final BookThumbnail bookThumbnail) {
            bookThumbnail.setThumbnail(context.getResources().getDrawable(R.drawable.no_book_thumbnail));
            String thumbnailId = bookThumbnail.getThumbnailId();
            new HtmlImageDownloader()
                    .loadBookThumbnail(thumbnailId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ThumbnailSubscriber(bookThumbnail, adapter));
            adapter.add(bookThumbnail);
        }
    }
}

package dmostek.cz.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.pnikosis.materialishprogress.ProgressWheel;

import dmostek.cz.library.libraryapi.BookDetail;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Detail of the book.
 */
public class DetailFragment extends Fragment {

    public static final String BOOK_ID_ARGUMENT = "bookId";

    private String bookId;
    private BookDetail detail;
    private ProgressWheel wheel;
    private ErrorView errorView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.detail_fragment, container, false);
        wheel = (ProgressWheel) layout.findViewById(R.id.progress_wheel);
        this.errorView = (ErrorView) layout.findViewById(R.id.error_view);
        if (detail == null) {
            bookId = this.getArguments().getString(BOOK_ID_ARGUMENT);
            load(layout);
        } else {
            mapDetail(layout);
        }
        return layout;
    }

    private void load(final View layout) {
        errorView.setVisibility(View.GONE);
        errorView.setListener(new OnRetryListener() {
            @Override
            public void onRetry() {
                DetailFragment.this.load(layout);
            }
        });
        wheel.setVisibility(View.VISIBLE);
        wheel.spin();
        ApplicationUtils.getApiFactory()
                .getBookDetailApi()
                .getTitleDetail(bookId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BookDetailSubscriber(layout));
    }

    private void mapDetail(View layout) {
        getActivity().setTitle(detail.getTitle());
        TextView author = (TextView) layout.findViewById(R.id.author);
        ExpandableTextView description = (ExpandableTextView) layout.findViewById(R.id.description);
        author.setText(detail.getAuthor());
        layout.findViewById(R.id.borrow_button).setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        author.setVisibility(View.VISIBLE);
        description.setText(detail.getDescription());
    }

    private class BookDetailSubscriber extends Subscriber<BookDetail> {

        private final View layout;

        public BookDetailSubscriber(View layout) {
            this.layout = layout;
        }

        @Override
        public void onCompleted() {
            unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            unsubscribe();
            wheel.stopSpinning();
            wheel.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNext(BookDetail bookDetail) {
            detail = bookDetail;
            wheel.stopSpinning();
            wheel.setVisibility(View.GONE);
            mapDetail(layout);
        }
    }
}

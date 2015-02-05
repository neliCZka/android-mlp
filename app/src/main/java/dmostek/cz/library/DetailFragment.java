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
import dmostek.cz.library.libraryapi.BookDetailApi;
import dmostek.cz.library.libraryapi.HtmlBookDetailApi;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mostek on 3.2.2015.
 */
public class DetailFragment extends Fragment {

    public static final String BOOK_ID_ARGUMENT = "bookId";

    private String bookId;
    private BookDetail detail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.detail_fragment, container, false);
        if (detail == null) {
            final ProgressWheel wheel = (ProgressWheel) layout.findViewById(R.id.progress_wheel);
            wheel.setVisibility(View.VISIBLE);
            wheel.spin();
            bookId = this.getArguments().getString(BOOK_ID_ARGUMENT);
            BookDetailApi htmlBookDetailApi = new HtmlBookDetailApi();
            htmlBookDetailApi.getBookDetail(bookId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BookDetail>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO log
                            wheel.stopSpinning();
                            wheel.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(BookDetail bookDetail) {
                            detail = bookDetail;
                            wheel.stopSpinning();
                            wheel.setVisibility(View.GONE);
                            mapDetail(layout);
                        }
                    });
        } else {
            mapDetail(layout);
        }
        return layout;
    }

    private void mapDetail(View layout) {
        TextView title = (TextView) layout.findViewById(R.id.title);
        TextView author = (TextView) layout.findViewById(R.id.author);
        ExpandableTextView description = (ExpandableTextView) layout.findViewById(R.id.description);
        title.setText(detail.getTitle());
        author.setText(detail.getAuthor());
        description.setText(detail.getDescription());

    }
}

package dmostek.cz.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private String bookId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.detail_fragment, container, false);
        bookId = this.getArguments().getString("bookId");

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
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BookDetail bookDetail) {
                        TextView title = (TextView) layout.findViewById(R.id.title);
                        title.setText(bookDetail.getTitle());
                    }
                });

        return layout;
    }
}

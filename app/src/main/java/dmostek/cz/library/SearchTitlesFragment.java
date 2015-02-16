package dmostek.cz.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by mostek on 28.1.2015.
 */
public class SearchTitlesFragment extends Fragment {

    private EditText searchInput;
    private String searchTerm;
    private BookSearchResultAdapter adapter;
    private RecyclerView listView;
    private OnBookSearchListener onBookSearchListener;
    private QucikReturnScrollListener qucikReturnScrollListener;
    private TextView.OnEditorActionListener keyboardSearchListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        BookThumbnailHolder.BookSelectedListener activity = (BookThumbnailHolder.BookSelectedListener) getActivity();
        adapter = new BookSearchResultAdapter(getActivity());
        adapter.setListener(activity);
        onBookSearchListener = new OnBookSearchListener(adapter, getActivity());
        qucikReturnScrollListener = new QucikReturnScrollListener();
        keyboardSearchListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onBookSearchListener.onClick(v);
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.search_titles, container, false);
        searchInput = (EditText) layout.findViewById(R.id.book_search);
        searchInput.setText(searchTerm);
        listView = (RecyclerView) layout.findViewById(R.id.search_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);
        View searchButton = layout.findViewById(R.id.search_button);
        onBookSearchListener.setView(layout);
        searchButton.setOnClickListener(onBookSearchListener);
        qucikReturnScrollListener.setReturningView(layout.findViewById(R.id.search_header));
        listView.setOnScrollListener(qucikReturnScrollListener);
        searchInput.setOnEditorActionListener(keyboardSearchListener);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        searchTerm = searchInput.getText().toString();
    }
}

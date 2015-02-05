package dmostek.cz.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by mostek on 28.1.2015.
 */
public class HomeFragment extends Fragment {

    private EditText searchInput;
    private String searchTerm;
    private BookSearchResultAdapter adapter;
    private RecyclerView listView;
    private OnBookSearchListener onBookSearchListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        BookThumbnailHolder.BookSelectedListener activity = (BookThumbnailHolder.BookSelectedListener) getActivity();
        adapter = new BookSearchResultAdapter(getActivity());
        adapter.setListener(activity);
        onBookSearchListener = new OnBookSearchListener(adapter, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.home_fragmenty, container, false);
        searchInput = (EditText) layout.findViewById(R.id.book_search);
        searchInput.setText(searchTerm);
        listView = (RecyclerView) layout.findViewById(R.id.search_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);
        Button b = (Button) layout.findViewById(R.id.search_button);
        onBookSearchListener.setView(layout);
        b.setOnClickListener(onBookSearchListener);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        searchTerm = searchInput.getText().toString();
    }
}

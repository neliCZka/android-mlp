package dmostek.cz.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import dmostek.cz.library.libraryapi.BookSearchItem;

/**
 * Recycler adapter for results of book search.
 */
public class BookSearchResultAdapter extends RecyclerView.Adapter<BookThumbnailHolder> {

    private final List<BookSearchItem> data = new ArrayList<>();
    private final Context context;
    private BookThumbnailHolder.BookSelectedListener listener;

    public BookSearchResultAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BookThumbnailHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.book_thumbnail, null);
        BookThumbnailHolder bookThumbnailHolder = new BookThumbnailHolder(root);
        bookThumbnailHolder.setListener(listener);
        return bookThumbnailHolder;
    }

    @Override
    public void onBindViewHolder(BookThumbnailHolder bookThumbnail, int i) {
        BookSearchItem item = data.get(i);
        bookThumbnail.setTitle(item.getName());
        bookThumbnail.setImage(item.getThumbnail());
        bookThumbnail.setBookId(item.getId());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(BookSearchItem s) {
        data.add(s);
    }

    public void clear() {
        data.clear();
    }

    public void itemChanged(BookSearchItem bookSearchItem) {
        int i1 = data.indexOf(bookSearchItem);
        notifyItemChanged(i1);
    }

    public void setListener(BookThumbnailHolder.BookSelectedListener activity) {
        this.listener = activity;
    }
}

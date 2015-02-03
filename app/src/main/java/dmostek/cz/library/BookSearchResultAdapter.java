package dmostek.cz.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mostek on 28.1.2015.
 */
public class BookSearchResultAdapter extends RecyclerView.Adapter<BookThumbnailHolder> {

    private final List<BookThumbnail> data = new ArrayList<>();
    private final Context context;

    public BookSearchResultAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BookThumbnailHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.book_thumbnail, null);
        return new BookThumbnailHolder(root);
    }

    @Override
    public void onBindViewHolder(BookThumbnailHolder bookThumbnail, int i) {
        BookThumbnail item = data.get(i);
        bookThumbnail.setTitle(item.getName());
        bookThumbnail.setImage(item.getThumbnail());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(BookThumbnail s) {
        data.add(s);
    }

    public void itemChanged(Integer first) {
        notifyItemChanged(first);
    }

    public BookThumbnail getItem(Integer first) {
        return data.get(first);
    }

    public void clear() {
        data.clear();
    }

    public void itemChanged(BookThumbnail bookThumbnail) {
        int i1 = data.indexOf(bookThumbnail);
        notifyItemChanged(i1);
    }

}

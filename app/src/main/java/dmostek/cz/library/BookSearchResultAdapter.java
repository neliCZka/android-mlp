package dmostek.cz.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dmostek.cz.library.libraryapi.BookSearchItem;

/**
 * Recycler adapter for results of book search.
 */
public class BookSearchResultAdapter extends RecyclerView.Adapter<BookThumbnailHolder> {

    private static final int ITEM_TYPE = 1;
    private static final int ITEM_TYPE_NEXT = 2;
    private static final int ITEM_TYPE_FIRST = 3;

    private final List<BookSearchItem> data = new ArrayList<>();
    private final Context context;
    private BookThumbnailHolder.BookSelectedListener listener;
    private PagingListener onPagingListener;

    public BookSearchResultAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BookThumbnailHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = null;
        BookThumbnailHolder bookThumbnailHolder = null;
        if (ITEM_TYPE_NEXT == viewType) {
            root = inflater.inflate(R.layout.book_thumbnail_with_pager, null);
            TextView next = (TextView) root.findViewById(R.id.next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPagingListener.onNextPage();
                }
            });
            bookThumbnailHolder = new BookThumbnailHolder(root);
            bookThumbnailHolder.setListener(listener);
        } else {
            root = inflater.inflate(R.layout.book_thumbnail, null);
            bookThumbnailHolder = new BookThumbnailHolder(root);
            bookThumbnailHolder.setListener(listener);
        }
        if (viewType == ITEM_TYPE_FIRST) {
            // First item has padding because of search bar
            root.setPadding(0, 100, 0, 0);
        }
        return bookThumbnailHolder;
    }

    @Override
    public void onBindViewHolder(BookThumbnailHolder bookThumbnail, int i) {
        BookSearchItem item = data.get(i);
        bookThumbnail.setTitle(item.getName());
        bookThumbnail.setImage(item.getThumbnail());
        bookThumbnail.setBookId(item.getId());
        bookThumbnail.setType(item.getType());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size() - 1) {
            return ITEM_TYPE_NEXT;
        } else if (position == 0) {
            return ITEM_TYPE_FIRST;
        } else {
            return ITEM_TYPE;
        }
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

    public void setOnPagingListener(PagingListener onPagingListener) {
        this.onPagingListener = onPagingListener;
    }
}

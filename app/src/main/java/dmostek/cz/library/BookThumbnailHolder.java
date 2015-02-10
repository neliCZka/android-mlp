package dmostek.cz.library;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dmostek.cz.library.libraryapi.SearchItemType;

/**
 * View Holder of the single book search result.
 */
public class BookThumbnailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView title;
    private final ImageView image;
    private String bookId;
    private BookSelectedListener listener;
    private SearchItemType type;

    public BookThumbnailHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        image = (ImageView) itemView.findViewById(R.id.book_thumbnail);
        itemView.setOnClickListener(this);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setImage(Drawable drawable) {
        image.setImageDrawable(drawable);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onBookSelected(bookId, type);
        }
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setListener(BookSelectedListener listener) {
        this.listener = listener;
    }

    public void setType(SearchItemType type) {
        this.type = type;
    }

    /**
     * Listener of the event of selecting book from search.
     */
    public interface BookSelectedListener {

        /**
         * Triggered when user selects book from search result list.
         *
         * @param id id of the selected book
         * @param type
         */
        public void onBookSelected(String id, SearchItemType type);
    }
}

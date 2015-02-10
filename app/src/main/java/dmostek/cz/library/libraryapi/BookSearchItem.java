package dmostek.cz.library.libraryapi;

import android.graphics.drawable.Drawable;

import java.net.URL;

/**
 * DTO of the single result of the book search.
 */
public class BookSearchItem {

    /**
     * ID of the book.
     */
    private String id;
    /**
     * Name of the book
     */
    private String name;
    /**
     * Cover thumbnail id.
     */
    private URL thumbnailUrl;
    /**
     * Actual book cover thumbnail.
     */
    private Drawable thumbnail;

    private SearchItemType type;

    public void setName(String s) {
        this.name = s;
    }

    public String getName() {
        return name;
    }

    public void setThumbnail(Drawable thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Drawable getThumbnail() {
        return thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setThumbnailUrl(URL thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public URL getThumbnailUrl() {
        return thumbnailUrl;
    }

    public SearchItemType getType() {
        return type;
    }

    public void setType(SearchItemType type) {
        this.type = type;
    }
}

package dmostek.cz.library;

import android.graphics.drawable.Drawable;

/**
 * Created by mostek on 29.1.2015.
 */
public class BookThumbnail {
    private String name;
    private Drawable thumbnail;
    private String id;

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
}

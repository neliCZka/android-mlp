package dmostek.cz.library.libraryapi;

/**
 * Created by mostek on 3.2.2015.
 */
public class BookDetail {
    private String title;
    private String author;
    private String description;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

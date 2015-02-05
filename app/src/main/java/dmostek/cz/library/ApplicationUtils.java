package dmostek.cz.library;

/**
 * Created by mostek on 5.2.2015.
 */
public class ApplicationUtils {

    private static final ApiFactory factory = new HtmlApiFactory();

    public static ApiFactory getApiFactory() {
        return factory;
    }
}

package dmostek.cz.library;

/**
 * Place for static methods used across the application.
 */
public class ApplicationUtils {

    /**
     * This is single point to change in case of changing the provider of API.
     */
    private static final ApiFactory factory = new HtmlApiFactory();

    /**
     * Get API provider.
     */
    public static ApiFactory getApiFactory() {
        return factory;
    }
}

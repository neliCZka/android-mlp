package dmostek.cz.library;

import java.io.IOException;

/**
 * Translates Exceptions to error codes.
 */
public class ExceptionTranslator {

    public static ErrorType translate(Throwable e) {
        if (e instanceof IOException) {
            return ErrorType.NETWORK_ERROR;
        }
        throw new RuntimeException("Unknown error", e);
    }
}

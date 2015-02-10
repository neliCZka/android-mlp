package dmostek.cz.library;

/**
 * Listener for retry event which should trigger previous action again.
 */
public interface OnRetryListener {

    public void onRetry();
}

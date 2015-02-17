package dmostek.cz.library;

import rx.Subscription;

/**
 * Null object for subscription. Use instead of setting subscription to null for initialization.
 */
public final class NullSubscription implements Subscription {

    @Override
    public void unsubscribe() {
        // NO CODE
    }

    @Override
    public boolean isUnsubscribed() {
        return true;
    }
}

package dmostek.cz.library;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

/**
 * Created by dominik.mostek on 16.2.2015.
 */
public class QucikReturnScrollListener extends RecyclerView.OnScrollListener {

    private View mReturningView;
    private static final int STATE_ONSCREEN = 0;
    private static final int STATE_OFFSCREEN = 1;
    private static final int STATE_RETURNING = 2;
    private int mState = STATE_ONSCREEN;
    private int mMinRawY = 0;
    private int mGravity = Gravity.BOTTOM;

    private int mScrolledY;
    private int lastTranslationY;

    public void setReturningView(View view) {
        mReturningView = view;
        try {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mReturningView.getLayoutParams();
            mGravity = params.gravity;
        } catch (ClassCastException e) {
            throw new RuntimeException("The return view need to be put in a FrameLayout");
        }
        animate(lastTranslationY);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if(mReturningView == null)
            return;
        int mReturningViewHeight = mReturningView.getHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mReturningView.getLayoutParams();
        mGravity = params.gravity;
        if(mGravity == Gravity.BOTTOM) {
            mScrolledY += dy;
        }
        else if(mGravity == -1) {
            mScrolledY -= dy;
        }
        int translationY = 0;
        int rawY = mScrolledY;
        switch (mState) {
            case STATE_OFFSCREEN:
                if(mGravity == Gravity.BOTTOM) {
                    if (rawY >= mMinRawY) {
                        mMinRawY = rawY;
                    } else {
                        mState = STATE_RETURNING;
                    }
                } else if(mGravity == -1) {
                    if (rawY <= mMinRawY) {
                        mMinRawY = rawY;
                    } else {
                        mState = STATE_RETURNING;
                    }
                }
                translationY = rawY;
                break;

            case STATE_ONSCREEN:
                if(mGravity == Gravity.BOTTOM) {

                    if (rawY > mReturningViewHeight) {
                        mState = STATE_OFFSCREEN;
                        mMinRawY = rawY;
                    }
                } else if(mGravity == -1) {

                    if (rawY < -mReturningViewHeight) {
                        mState = STATE_OFFSCREEN;
                        mMinRawY = rawY;
                    }
                }
                translationY = rawY;
                break;

            case STATE_RETURNING:
                if(mGravity == Gravity.BOTTOM) {
                    translationY = (rawY - mMinRawY) + mReturningViewHeight;

                    if (translationY < 0) {
                        translationY = 0;
                        mMinRawY = rawY + mReturningViewHeight;
                    }

                    if (rawY == 0) {
                        mState = STATE_ONSCREEN;
                        translationY = 0;
                    }

                    if (translationY > mReturningViewHeight) {
                        mState = STATE_OFFSCREEN;
                        mMinRawY = rawY;
                    }
                } else if(mGravity == -1) {
                    translationY = (rawY + Math.abs(mMinRawY)) - mReturningViewHeight;

                    if (translationY > 0) {
                        translationY = 0;
                        mMinRawY = rawY - mReturningViewHeight;
                    }

                    if (rawY == 0) {
                        mState = STATE_ONSCREEN;
                        translationY = 0;
                    }

                    if (translationY < -mReturningViewHeight) {
                        mState = STATE_OFFSCREEN;
                        mMinRawY = rawY;
                    }
                }
                break;
        }
        animate(translationY);
    }

    private void animate(int translationY) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
            TranslateAnimation anim = new TranslateAnimation(0, 0, translationY, translationY);
            anim.setFillAfter(true);
            anim.setDuration(0);
            mReturningView.startAnimation(anim);
        } else {
            mReturningView.setTranslationY(translationY);
        }
        lastTranslationY = translationY;
    }
}

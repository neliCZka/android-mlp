package dmostek.cz.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mostek on 10.2.2015.
 */
public class ErrorView extends LinearLayout {

    private final Context context;
    private final AttributeSet attrs;
    private TextView retryButton;
    private TextView errorSubtitle;
    private ImageView errorImage;
    private boolean showImage;
    private boolean showRetryButton;
    private OnRetryListener listener;

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }

    private void init() {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ErrorView, 0, 0);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.error_view, this, true);
        errorImage = (ImageView) findViewById(R.id.error_image);
        errorSubtitle = (TextView) findViewById(R.id.error_subtitle);
        retryButton = (TextView) findViewById(R.id.error_retry);
        try {
            showImage = a.getBoolean(R.styleable.ErrorView_ev_showImage, true);
            showRetryButton = a.getBoolean(R.styleable.ErrorView_ev_showRetryButton, true);
            if (!showImage) {
                errorImage.setVisibility(GONE);
            }
            if (!showRetryButton) {
                retryButton.setVisibility(GONE);
            }
        } finally {
            a.recycle();
        }

        retryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onRetry();
                }
            }
        });
    }

    public void setListener(OnRetryListener listener) {
        this.listener = listener;
    }

    public void setErrorSubtitle(TextView errorSubtitle) {
        this.errorSubtitle = errorSubtitle;
    }

    public void setErrorTitle(String title) {
        errorSubtitle.setText(title);
    }

}

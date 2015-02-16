package dmostek.cz.library;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class BookDetailActivity extends ActionBarActivity {

    private static final String TAG_TASK_FRAGMENT = "detail_fragment";
    public static final String BOOK_ID_PARAM = "bookId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            String bookId = getIntent().getStringExtra(BOOK_ID_PARAM);
            Bundle bundle = new Bundle();
            bundle.putString("bookId", bookId);
            DetailFragment mTaskFragment = new DetailFragment();
            mTaskFragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.detail_fragment, mTaskFragment, TAG_TASK_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

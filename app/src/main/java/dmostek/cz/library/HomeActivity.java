package dmostek.cz.library;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class HomeActivity extends ActionBarActivity implements BookThumbnailHolder.BookSelectedListener {

    private static final String TAG_TASK_FRAGMENT = "home_search_fragment";
    private HomeFragment mTaskFragment;
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (HomeFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (mTaskFragment == null) {
            mTaskFragment = new HomeFragment();
            fm.beginTransaction()
                    .add(R.id.home_search_fragment, mTaskFragment, TAG_TASK_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBookSelected(String id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        FragmentManager fm = getSupportFragmentManager();
        detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("bookId", id);
        detailFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.home_search_fragment, detailFragment)
                .addToBackStack("detail").commit();

    }
}

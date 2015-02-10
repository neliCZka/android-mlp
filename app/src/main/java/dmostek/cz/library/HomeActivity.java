package dmostek.cz.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import dmostek.cz.library.libraryapi.BookDetail;

public class HomeActivity extends ActionBarActivity implements BookThumbnailHolder.BookSelectedListener {

    private static final String TAG_TASK_FRAGMENT = "home_search_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            HomeFragment mTaskFragment = new HomeFragment();
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
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("bookId", id);
        startActivity(intent);
    }
}

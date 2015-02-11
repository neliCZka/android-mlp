package dmostek.cz.library;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import dmostek.cz.library.libraryapi.SearchItemType;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

public class HomeActivity extends MaterialNavigationDrawer implements BookThumbnailHolder.BookSelectedListener {

    private static final String TAG_TASK_FRAGMENT = "home_search_fragment";

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//        FragmentManager fm = getSupportFragmentManager();
//        if (savedInstanceState == null) {
//            SearchTitlesFragment mTaskFragment = new SearchTitlesFragment();
//            fm.beginTransaction()
//                    .add(R.id.home_search_fragment, mTaskFragment, TAG_TASK_FRAGMENT)
//                    .commit();
//        }
//    }

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
    public void init(Bundle bundle) {
        MaterialSection section = newSection("Test", new SearchTitlesFragment());
        section.setTarget(new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                MaterialSection currentSection = getCurrentSection();
                boolean equals = materialSection.equals(currentSection);
            }
        });
        this.addSection(section);
        this.addBottomSection(newSection("Bottom Section", R.drawable.ic_settings_black_24dp, new Intent(this, Settings.class)));
    }

    @Override
    public void onBookSelected(String id, SearchItemType type) {
        if (type == SearchItemType.TITLE) {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra("bookId", id);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Book click not implemented for this type", Toast.LENGTH_SHORT).show();
        }
    }
}

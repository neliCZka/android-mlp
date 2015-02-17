package dmostek.cz.library;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import java.util.List;

import dmostek.cz.library.libraryapi.SearchItemType;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class HomeActivity extends MaterialNavigationDrawer implements BookThumbnailHolder.BookSelectedListener {

    @Override
    public void init(Bundle bundle) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = supportFragmentManager.getFragments();
        SearchTitlesFragment searchFragment = null;
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof SearchTitlesFragment) {
                    searchFragment = (SearchTitlesFragment) fragment;
                }
            }
        }
        if (searchFragment == null) {
            searchFragment = new SearchTitlesFragment();
        }
        setDrawerHeaderImage(R.drawable.header);
        MaterialSection section = newSection(getString(R.string.search), R.drawable.search_without_bck, searchFragment);
        this.addSection(section);
        this.addBottomSection(newSection(getString(R.string.action_settings), R.drawable.settings_icon, new Intent(this, Settings.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        closeDrawer();
    }

    @Override
    public void onBackPressed() {
        if (super.isDrawerOpen()) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBookSelected(String id, SearchItemType type) {
        if (type == SearchItemType.TITLE) {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(BookDetailActivity.BOOK_ID_PARAM, id);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Click not implemented for this type", Toast.LENGTH_SHORT).show();
        }
    }
}

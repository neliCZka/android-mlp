package dmostek.cz.library;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by mostek on 11.2.2015.
 */
public class Settings extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.settiogs_view);
        setTitle("Settings");
        setTheme(R.style.Theme_WithoutDrawer);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}

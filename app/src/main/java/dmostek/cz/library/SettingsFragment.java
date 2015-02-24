package dmostek.cz.library;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by dominik.mostek on 17.2.2015.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}

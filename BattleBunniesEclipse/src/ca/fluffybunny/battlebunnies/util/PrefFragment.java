package ca.fluffybunny.battlebunnies.util;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import ca.fluffybunny.battlebunnies.R;


public class PrefFragment extends PreferenceFragment {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.profile);           
	}

}

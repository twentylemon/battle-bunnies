package ca.fluffybunny.battebunnies.activities;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import ca.fluffybunny.battlebunnies.R;


public class ProfileActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();
    }	

	
	@SuppressLint("ValidFragment")
	private class PrefFragment extends PreferenceFragment {
		public void onCreate(Bundle savedInstanceState){
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.profile);
		}
	}
}

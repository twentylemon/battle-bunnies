package ca.fluffybunny.battebunnies.activities;
import ca.fluffybunny.battlebunnies.util.PrefFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Profile extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();
    }	
	

}

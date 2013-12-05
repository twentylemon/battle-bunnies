package ca.fluffybunny.battebunnies.activities;



import com.fluffybunny.battlebunnies.activities.MultiplayerLaunchActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import ca.fluffybunny.battlebunnies.R;
import ca.fluffybunny.battlebunnies.util.ConnectionHandler;

public class MainActivity extends Activity {
	public static final String TAG = "fluffybunny";
	
	public static final String PREFERENCES = "ca.fluffybunny.battebunnies";

	private static final int REQUEST_CONNECT_DEVICE = 1;
	private BluetoothAdapter mBluetoothAdapter = null;
	public ConnectionHandler conHandle;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    
    
    /**
     * Returns the image resource for a bunny given their id in shared preferences.
     * 
     * @param key id in array.xml -> charactervals
     * @return resource image id
     */
    private int getImage(int key){
    	switch (key){
    	case 0: return R.drawable.player_bunny;
    	case 1: return R.drawable.player2_bunny;
    	case 2: return R.drawable.macarthur_bunny;
    	case 3: return R.drawable.nelson_bunny;
    	case 4: return R.drawable.caeser_bunny;
    	case 5: return R.drawable.leonidus_bunny;
    	case 6: return R.drawable.russian_bunny;
    	case 7: return R.drawable.rommel_fox;
    	}
    	return R.drawable.player_bunny;
    }

    
    /**
     * Called when Profile is clicked. Starts up the settings activity.
     *
     * @param view the button clicked
     */
    public void onProfileClicked(View view){
    	Intent j = new Intent(MainActivity.this, ProfileActivity.class);
        startActivityForResult(j, 0);
    }


    /**
     * Called when the Campaign button is clicked. Starts the campaign loader activity.
     *
     * @param view the button clicked
     */
    public void onCampaignClicked(View view){
        //start the campaign loader activity
    	SharedPreferences prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    	String name = prefs.getString("name", getString(R.string.flopsy));
    	int key = prefs.getInt("bunny", 0);
    	int progress = prefs.getInt("progress", 2);
    	int[] images = { getImage(key), getImage(progress) };
    	String[] names = { name, "AI" };
    }

    /**
     * Called when Quick Play is clicked. Starts up the weapon select activity.
     *
     * @param view the button clicked
     */
    public void onQuickPlayClicked(View view){
        //start the weapon select activity
    	Intent intent = new Intent(this, GameActivity.class);
    	int[] images = { R.drawable.player_bunny, R.drawable.player2_bunny };
    	String[] names = { "Fluffy", "Bunny" };
    	intent.putExtra(GameActivity.PLAYER_IMAGES, images);
    	intent.putExtra(GameActivity.PLAYER_NAMES, names);
    	startActivity(intent);
    }


    /**
     * Called when mutliplayer is clicked. Starts up the bluetooth handler activity.
     *
     * @param view the button clicked
     */
    public void onMultiplayerClicked(View view){
    	Intent j = new Intent(MainActivity.this, MultiplayerLaunchActivity.class);
        startActivity(j);
    }
}

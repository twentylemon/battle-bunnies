package ca.fluffybunny.battebunnies.activities;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import ca.fluffybunny.battlebunnies.R;
import ca.fluffybunny.battlebunnies.util.ConnectionHandler;

public class MainActivity extends Activity {
	public static final String TAG = "fluffybunny";

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
    }

    /**
     * Called when Quick Play is clicked. Starts up the weapon select activity.
     *
     * @param view the button clicked
     */
    public void onQuickPlayClicked(View view){
        //start the weapon select activity
    	Intent intent = new Intent(this, GameActivity.class);
    	String[] names = { "Fluffy", "Bunny" };
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

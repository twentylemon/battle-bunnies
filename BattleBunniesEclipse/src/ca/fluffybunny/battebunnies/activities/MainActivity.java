package ca.fluffybunny.battebunnies.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import ca.fluffybunny.battlebunnies.R;

public class MainActivity extends Activity {

	private static final int REQUEST_CONNECT_DEVICE = 1;
	private BluetoothAdapter mBluetoothAdapter = null;
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
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            // Launch the DeviceListActivity to see devices and do scan
        	ensureDiscoverable();
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        }
        return false;
    }
    
    private void ensureDiscoverable() {
        if (mBluetoothAdapter != null &&
        		mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
        
    




    /**
     * Called when Profile is clicked. Starts up the settings activity.
     *
     * @param view the button clicked
     */
    public void onProfileClicked(View view){
    	Intent j = new Intent(MainActivity.this, Profile.class);
        startActivityForResult(j,0);
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
    }


    /**
     * Called when mutliplayer is clicked. Starts up the bluetooth handler activity.
     *
     * @param view the button clicked
     */
    public void onMultiplayerClicked(View view){
        //start the bluetooth activity
    }
}

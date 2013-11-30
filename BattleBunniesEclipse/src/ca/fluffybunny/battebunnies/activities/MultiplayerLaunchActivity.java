package ca.fluffybunny.battebunnies.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import ca.fluffybunny.battlebunnies.R;
import ca.fluffybunny.battlebunnies.R.id;
import ca.fluffybunny.battlebunnies.R.layout;
import ca.fluffybunny.battlebunnies.R.menu;
import ca.fluffybunny.battlebunnies.util.ConnectionHandler;

public class MultiplayerLaunchActivity extends Activity {
	
	Button blue;
	Button host;
	Button join;
	Button enable;
	private BluetoothAdapter mBluetoothAdapter = null;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	public ConnectionHandler conHandle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_launch);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        conHandle= new ConnectionHandler(this);
        
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        
		host = (Button) findViewById(R.id.button1);
		join = (Button) findViewById(R.id.button2);
		blue = (Button) findViewById(R.id.button3);
		enable = (Button) findViewById(R.id.button4);
		 
		host.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) { 
				if(mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
					Toast.makeText(MultiplayerLaunchActivity.this, "Make Discoverable to Host", Toast.LENGTH_LONG).show();
		        	return;
		        }
				else if (conHandle.mState == ConnectionHandler.STATE_NONE) {
		              
					conHandle.start();
		            }; //set up game, start as server
			} 
		});
		
		join.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) { 
				Intent serverIntent = new Intent(MultiplayerLaunchActivity.this, DeviceListActivity.class);
	            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	                        
			} 
		});
		blue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) { 
				mBluetoothAdapter.enable();
			} 
		});
		enable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) { 
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
	            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
	            startActivity(discoverableIntent);
			} 
		});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.multi_launch, menu);
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                conHandle.connect(device);
                
                if(conHandle.getBluetoothSocket()!=null){
                	Toast.makeText(this, "Connecting to "+device.getName(), Toast.LENGTH_LONG).show();                	
                	return;
                }
                
                if(conHandle.getBluetoothSocket()!=null){
                	Toast.makeText(this, "Connection Established ", Toast.LENGTH_LONG).show();                	
                	return;
                }
                
              //get game info from host
	          //start game as non server
            }
            break;
        }
    }
	
	
}

package com.fluffybunny.battlebunnies.activities;

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

import com.fluffybunny.battlebunnies.R;

public class MultiplayerLaunchActivity extends Activity {
	
	Button blue;
	Button host;
	Button join;
	Button enable;
	private BluetoothAdapter mBluetoothAdapter = null;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_launch);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
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
				Toast.makeText(getApplicationContext(), "waiting for another player", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(MultiplayerLaunchActivity.this, MultiplayerActivity.class);  
				intent.putExtra(MultiplayerActivity.IS_SERVER, true);
            	startActivity(intent);
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
		getMenuInflater().inflate(R.menu.multiplayer, menu);
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
                
                Intent intent = new Intent(this, MultiplayerActivity.class);            	
            	intent.putExtra(MultiplayerActivity.BLUETOOTH_DEVICE, device); 
            	intent.putExtra(MultiplayerActivity.IS_SERVER, false);
            	startActivity(intent);
                
              
            }
            break;
        }
    }
	
	
}

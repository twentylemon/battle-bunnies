package com.fluffybunny.battlebunnies.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	private String[] names = { "server", "client" };
	private int[] images = { R.drawable.player_bunny, R.drawable.player2_bunny };
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_launch);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        if (mBluetoothAdapter == null){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
		host = (Button) findViewById(R.id.button1);
		join = (Button) findViewById(R.id.button2);
		blue = (Button) findViewById(R.id.button3);
		enable = (Button) findViewById(R.id.button4);
		 
		host.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){ 
				if(mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
					Toast.makeText(MultiplayerLaunchActivity.this, "Make Discoverable to Host", Toast.LENGTH_LONG).show();
		        	return;
		        }
				Intent intent = new Intent(MultiplayerLaunchActivity.this, GameActivityMP.class);  
				intent.putExtra(GameActivityMP.IS_SERVER, true);
				intent.putExtra(GameActivitySP.PLAYER_NAMES, names);
				intent.putExtra(GameActivitySP.PLAYER_IMAGES, images);
				Log.e("client", "I am the server");
            	startActivity(intent);
			} 
		});
		
		join.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){
				Intent serverIntent = new Intent(MultiplayerLaunchActivity.this, DeviceListActivity.class);
	            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	                        
			} 
		});

		blue.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){ 
				mBluetoothAdapter.enable();
			} 
		});
		enable.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){ 
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
	            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
	            startActivity(discoverableIntent);
			} 
		});
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
        case REQUEST_CONNECT_DEVICE:
            //when DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK){
                //get the device MAC address
                String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                //get the BluetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                
                Intent intent = new Intent(this, GameActivityMP.class);            	
            	intent.putExtra(GameActivityMP.REMOVE_DEVICE_ADDRESS, device.getAddress()); 
            	intent.putExtra(GameActivityMP.IS_SERVER, false);
				intent.putExtra(GameActivitySP.PLAYER_NAMES, names);
				intent.putExtra(GameActivitySP.PLAYER_IMAGES, images);
				Log.e("client", "I am the client");
            	startActivity(intent);
            }
            break;
        }
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.multiplayer, menu);
		return true;
	}
}

package com.fluffybunny.battlebunnies.activities;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.fluffybunny.battlebunnies.R;

public class MainActivity extends Activity {
	
	public static final String TAG = "BattleBunnies";
	public static final String PREFERENCES = "com.fluffybunny.battlebunnies";
	public static final int NUM_BUNNIES = 8;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
    	//Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        //startActivity(intent);
    }


    /**
     * Called when the Campaign button is clicked. Starts the campaign loader activity.
     *
     * @param view the button clicked
     */
    public void onCampaignClicked(View view){
        //start the campaign loader activity
    	/*
    	SharedPreferences prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    	String name = prefs.getString("name", getString(R.string.flopsy));
    	int key = prefs.getInt("bunny", 0);
    	int progress = prefs.getInt("progress", 2);
    	int[] images = { getImage(key), getImage(progress) };
    	String[] names = { name, "AI" };
    	*/
    }

    
    /**
     * Called when Quick Play is clicked. Starts up the weapon select activity.
     *
     * @param view the button clicked
     */
    public void onQuickPlayClicked(View view){
        //start the weapon select activity
    	Intent intent = new Intent(this, GameActivitySP.class);
    	int[] images = { R.drawable.player_bunny, getImage(new Random().nextInt(NUM_BUNNIES-1) + 1) };
    	String[] names = { "Fluffy", "Bunny" };
    	intent.putExtra(GameActivitySP.PLAYER_IMAGES, images);
    	intent.putExtra(GameActivitySP.PLAYER_NAMES, names);
    	startActivity(intent);
    }


    /**
     * Called when mutliplayer is clicked. Starts up the bluetooth handler activity.
     *
     * @param view the button clicked
     */
    public void onMultiplayerClicked(View view){
    	Intent intent = new Intent(MainActivity.this, MultiplayerLaunchActivity.class);
        startActivity(intent);
    }
}

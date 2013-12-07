package com.fluffybunny.battlebunnies.activities;

import com.fluffybunny.battlebunnies.R;
import com.fluffybunny.battlebunnies.R.drawable;
import com.fluffybunny.battlebunnies.R.id;
import com.fluffybunny.battlebunnies.R.layout;
import com.fluffybunny.battlebunnies.R.menu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	
	SharedPreferences prefs;
	public static String PLAYER_NAME= "playerName";
	public static String PLAYER_CHARACTER= "playerChar";
	public static String PLAYER_WINS= "playerWins";
	public static String PLAYER_LOSS= "playerLoss";
	public static String PLAYER_LEV= "playerLev";
	
	protected TextView winView;
	protected TextView lossView;
	protected TextView levView;
	protected EditText nameEdit;
	protected Spinner chars;
	protected Button save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		prefs= getPreferences(MODE_PRIVATE);
		setContentView(R.layout.activity_profile);
	}
	
	private void setupPrefs(){
		String name = prefs.getString(PLAYER_NAME, "Rusty");
		int wins = prefs.getInt(PLAYER_WINS, 0);
		int loss = prefs.getInt(PLAYER_LOSS, 0);
		int level = prefs.getInt(PLAYER_LEV, 0);
		
		winView= (TextView) findViewById(R.id.wins);	
		lossView= (TextView) findViewById(R.id.loss);	
		levView= (TextView) findViewById(R.id.lev);	
		nameEdit= (EditText) findViewById(R.id.nameEntry);	
		chars =(Spinner) findViewById(R.id.chars);
		
		setupSpinner();
		
		save = (Button) findViewById(R.id.saveButton);	
		save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){ 
				SharedPreferences.Editor editor = prefs.edit();
			    editor.putString(PLAYER_NAME, nameEdit.getText().toString());
			}
		});
		
		winView.setText(wins);
		lossView.setText(loss);
		levView.setText(level);		
		nameEdit.setText(name);
		
	}
	
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
	
	private void setupSpinner(){
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}

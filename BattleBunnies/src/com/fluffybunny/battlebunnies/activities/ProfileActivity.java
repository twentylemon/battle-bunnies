package com.fluffybunny.battlebunnies.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffybunny.battlebunnies.R;

public class ProfileActivity extends Activity {
	
	SharedPreferences prefs;
	public static String PLAYER_NAME= "playerName";
	public static String PLAYER_CHARACTER= "playerChar";
	public static String PLAYER_WINS= "playerWins";
	public static String PLAYER_LOSS= "playerLoss";
	public static String PLAYER_LEV= "playerLev";
	public static String PLAYER_IMAGE= "player_image";
	public static final String PREFS_NAME = "MyPrefsFile";
	
	protected TextView winView;
	protected TextView lossView;
	protected TextView levView;
	protected EditText nameEdit;
	protected Spinner chars;
	protected Button save;
	private List<String> namesArr;
	private int imageLoc;
	protected ImageView bunny;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		prefs= getSharedPreferences(PREFS_NAME, 0);
		setContentView(R.layout.activity_profile);
		imageLoc=0;
		setupPrefs();
	}
	
	private void setupPrefs(){
		String name = prefs.getString(PLAYER_NAME, "Rusty");
		int wins = prefs.getInt(PLAYER_WINS, 0);
		int loss = prefs.getInt(PLAYER_LOSS, 0);
		int level = prefs.getInt(PLAYER_LEV, 0);
		imageLoc = prefs.getInt(PLAYER_IMAGE, 0);
		
		System.out.println("ImageLoc: "+imageLoc);
		
		winView= (TextView) findViewById(R.id.wins);	
		lossView= (TextView) findViewById(R.id.loss);	
		levView= (TextView) findViewById(R.id.lev);	
		nameEdit= (EditText) findViewById(R.id.nameEntry);	
		chars =(Spinner) findViewById(R.id.chars);
		bunny= (ImageView) findViewById(R.id.bunnyIm);
		
		setupSpinner();
		
		save = (Button) findViewById(R.id.saveButton);	
		save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){ 
				SharedPreferences.Editor editor = prefs.edit();
			    editor.putString(PLAYER_NAME, nameEdit.getText().toString());
			    editor.putInt(PLAYER_IMAGE, imageLoc);
			    editor.commit();
			    Toast.makeText(getApplicationContext(), "Profile Saved.", Toast.LENGTH_LONG).show();
			}
		});
		
		winView.setText(""+wins);
		lossView.setText(""+loss);
		levView.setText(""+level);		
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
		namesArr = new ArrayList<String>();				
		namesArr.add("Blue Bunny");
		namesArr.add("Red Bunny");
		namesArr.add("MacFluffy");
		namesArr.add("Lord Admiral Flufson");
		namesArr.add("Bunius Ceaser");
		namesArr.add("Flufonidus");		
		namesArr.add("Bunnyovitch");
		namesArr.add("Erwin \"The Desert Fox\" Rommel");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,namesArr);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chars.setAdapter(dataAdapter);
		chars.setSelection(imageLoc);
		chars.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	System.out.println("position: "+ position);
		    	imageLoc= position;
		    	bunny.setImageResource(getImage(position));
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}

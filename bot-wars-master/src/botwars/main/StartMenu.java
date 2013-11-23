package botwars.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**********************************************************************************
 * 
 * Start Menu activity
 * 
 **********************************************************************************/

public class StartMenu extends Activity implements OnClickListener {
	public static boolean settingsChanged = false;

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		if (!settingsChanged) { // BotWars.setMap(1);
			BotWars.setVelocity(8.0f);
			BotWars.setImpulse(14.0f);
			BotWars.setMusicVolume(1.0f);
			BotWars.enableMusic(true);
			BotWars.enableSounds(true);
		}
		// settingsChanged=false;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startmenu);

		Typeface tf = Typeface.createFromAsset(getAssets(), "ANKLEPAN.TTF");
		Typeface tf1 = Typeface.createFromAsset(getAssets(), "AltamonteNF.ttf");

		TextView txv_start = (TextView) findViewById(R.id.txv_start);
		TextView txv_quick = (TextView) findViewById(R.id.txv_quick);
		TextView txv_settings = (TextView) findViewById(R.id.txv_settings);
		TextView txv_about = (TextView) findViewById(R.id.txv_about);
		TextView txv_exit = (TextView) findViewById(R.id.txv_exit);
		TextView txv_title = (TextView) findViewById(R.id.txv_title);

		txv_start.setTypeface(tf);
		txv_quick.setTypeface(tf);
		txv_settings.setTypeface(tf);
		txv_about.setTypeface(tf);
		txv_exit.setTypeface(tf);
		txv_title.setTypeface(tf1);

		txv_start.setOnTouchListener(new MenuTextTouchListener());
		txv_quick.setOnTouchListener(new MenuTextTouchListener());
		txv_settings.setOnTouchListener(new MenuTextTouchListener());
		txv_about.setOnTouchListener(new MenuTextTouchListener());
		txv_exit.setOnTouchListener(new MenuTextTouchListener());

		txv_start.setOnClickListener(this);
		txv_quick.setOnClickListener(this);
		txv_settings.setOnClickListener(this);
		txv_about.setOnClickListener(this);
		txv_exit.setOnClickListener(this);

		// Toast.makeText(this, "Press Menu to Start Game",
		// Toast.LENGTH_LONG).show();

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.txv_start:
			Intent openMapsMenu = new Intent(this, MapMenu.class);
			startActivity(openMapsMenu);
			finish();
			break;
			
		case R.id.txv_quick:
			makeMultiplayerModeDialog();			
			break;
			
		case R.id.txv_settings:
			Intent openSettingsMenu = new Intent(this, SettingsMenu.class);
			startActivity(openSettingsMenu);
			finish();
			break;
			
		case R.id.txv_about:
			makeAboutDialog();
			break;
			
		case R.id.txv_exit:
			finish();
			break;
		}

	}

	private void makeAboutDialog() {
		 // Linkify the message
	    final SpannableString strAbout = new SpannableString("http://www.code.google.com/p/bot-wars \n \nThis Game has been made by Dipen,Gaurav & Mayuresh");
	    Linkify.addLinks(strAbout, Linkify.ALL);


		AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(this);

		aboutDialogBuilder.setMessage(strAbout);//"http://www.code.google.com/p/bot-wars \n \nThis Game has been made by Dipen,Gaurav & Mayuresh");
		
		aboutDialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});
		
		AlertDialog aboutDialog=aboutDialogBuilder.create();
		
		aboutDialog.show();

	    // Make the textview clickable. Must be called after show()
	    ((TextView)aboutDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
	}

	
	private void makeMultiplayerModeDialog() {

		AlertDialog.Builder multiplayerModeDialog= new AlertDialog.Builder(this);
		
		multiplayerModeDialog.setMessage("Select Multiplayer Mode");
		
		multiplayerModeDialog.setPositiveButton("Internet", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Intent openMP_MapMenu_UDP = new Intent(StartMenu.this, MapMenu_UDP.class);
				finish();
				startActivity(openMP_MapMenu_UDP);
			
			}
		});

		multiplayerModeDialog.setNegativeButton("Bluetooth", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				
				Intent openMP_MapMenu_BT = new Intent(StartMenu.this, MapMenu_BT.class);
				finish();
				startActivity(openMP_MapMenu_BT);
		
			}
		});

		multiplayerModeDialog.show();
	}
	
	public static void settingsChanged() {
		settingsChanged = true;
	}
	// ImageView imageView= (ImageView)findViewById(R.id.imageview);
	// Animation fadeInAnimation = AnimationUtils.loadAnimation(this,
	// R.layout.anim);
	// Now Set your animation
	// imageView.startAnimation(fadeInAnimation );
	private class MenuTextTouchListener implements View.OnTouchListener 
	{     
	    public boolean onTouch(View view, MotionEvent motionEvent) {
	    
		    switch(motionEvent.getAction()){            
	            case MotionEvent.ACTION_DOWN:
	             ((TextView) view).setTextColor(0xFF6A5ceD); 
	                break;          
	            case MotionEvent.ACTION_CANCEL:             
	            case MotionEvent.ACTION_UP:
	            ((TextView) view).setTextColor(0xF9f9f9f9);
	                break;
		    } 
	     
	        return false;   
	    } 
	}

}

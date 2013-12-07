package com.fluffybunny.battlebunnies.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffybunny.battlebunnies.R;
import com.fluffybunny.battlebunnies.game.FireAction;
import com.fluffybunny.battlebunnies.game.GameCanvas;
import com.fluffybunny.battlebunnies.game.GameInfo;
import com.fluffybunny.battlebunnies.game.MoveAction;
import com.fluffybunny.battlebunnies.game.RandomGenerator;
import com.fluffybunny.battlebunnies.game.Terrain;
import com.fluffybunny.battlebunnies.game.Weapon;

public class GameActivitySP extends Activity {
	
	public static final String PLAYER_NAMES = "playerNames";
	public static final String PLAYER_IMAGES = "playerImages";
	public static final String TERRAIN_TYPE = "terrainType";
	public static final String AI_DIFFICULTY = "aiDifficulty";
	
	public static final int TERRAIN_TYPE_RANDOM = 0;
	
	protected SurfaceView gameView;
	protected SurfaceHolder surfaceHolder;
	protected SeekBar power;
	protected SeekBar angle;
	protected Button fire;
	protected Button moveLeft;
	protected Button moveRight;
	protected Spinner weaponSpinner;
	protected TextView powerText;
	protected TextView angleText;

	protected int shotPower;
	protected int shotAngle;
	protected int aiDifficulty;
	protected int terrainType;
	protected int[] playerImages;
	protected String[] playerNames;
	
	protected GameInfo game;
	protected GameCanvas gameCanvas;
	protected android.graphics.Point size;
	protected Terrain.Generator generator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_activity_sp);
		RelativeLayout conts= (RelativeLayout) findViewById(R.id.controler);
		conts.setBackgroundResource(R.drawable.background_red);
		setup();
		
		game = makeGameInfo();
		
		populateSpinner(game.getWeaponList());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.game_activity_s, menu);
		return true;
	}
	
	@Override
	public void onBackPressed(){
		if (gameCanvas != null){
			gameCanvas.stop();
		}
		super.onBackPressed();
	}
	
	
	/**
	 * Blocks the UI thread waiting for the canvas to finish firing.
	 */
	protected void waitForCanvas(){
		while (gameCanvas.isFiring()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e){}
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e){}
	}
	
	
	/**
	 * Returns the currently selected weapon id.
	 * 
	 * @return the current selected weapon
	 */
	protected int getSelectedWeapon(){
		String selected = (String) weaponSpinner.getSelectedItem();
		//index of should be searching for a weapon
		int id = -1;
		for (Weapon w : game.getWeaponList()){
			id++;
			if (w.getName().equals(selected)){
				return id;
			}
		}
		return 0;
	}


	/**
	 * Moves the bunny specified.
	 * 
	 * @param playerID the bunny to move
	 * @param left true if they are moving left, false if right
	 */
	protected void moveBunny(int playerID, boolean left){
		if (game.getBunny(playerID).getMovesRemaining() <= 0){
			Toast.makeText(getApplicationContext(), "No more moves remaining.", Toast.LENGTH_LONG).show();
		}
		else {
			new MoveAction(playerID, left).execute(game);
		}
	}
	
	
	/**
	 * Performs the player's turn.
	 */
	protected void myTurn(){
		FireAction action = new FireAction(game.getMyID(), shotPower, shotAngle, getSelectedWeapon());
		action.execute(game);
		waitForCanvas();
	}
	
	
	/**
	 * Performs the other player's turn. In single player, the AI takes his turn.
	 */
	protected void otherTurn(){
		Random rand = new Random();
		new FireAction(1, 80 + rand.nextInt(80), 90 + rand.nextInt(60), rand.nextInt(game.getNumWeapons())).execute(game);
		gameCanvas.setFiring(true);
	}
	
	
	/**
	 * Checks for the game being over. If it is, the activity ends.
	 */
	protected void checkEndGame(){
		if (game.isGameOver()){
			waitForCanvas();
			gameCanvas.stop();
			int myScore = game.getBunny(game.getMyID()).getScore();
			int aiScore = game.getBunny(game.otherID(game.getMyID())).getScore();
			if (myScore > aiScore){
				Toast.makeText(getApplicationContext(), "You won! " + myScore + " to " + aiScore, Toast.LENGTH_LONG).show();
			}
			else if (myScore < aiScore){
				Toast.makeText(getApplicationContext(), "Failure. " + myScore + " to " + aiScore, Toast.LENGTH_LONG).show();
			}
			else {	//tie game
				Toast.makeText(getApplicationContext(), "You may or may not have won.", Toast.LENGTH_LONG).show();
			}
			onBackPressed();
		}
	}
	
	
	/**
	 * Handles the fire button being pressed.
	 * The entire turn gets played out when fire is pressed.
	 */
	protected void firePressed(){
		if (!gameCanvas.isFiring()){
			myTurn();
			otherTurn();
			checkEndGame();
		}
	}
	

	/**
	 * Initializes the spinner with the list of weapons.
	 * 
	 * @param weaponList the list of all the weapons
	 */
	protected void populateSpinner(List<Weapon> weaponList){
		weaponSpinner = (Spinner) findViewById(R.id.spinner1);
		List<String> weap = new ArrayList<String>();
		for (Weapon w : weaponList){
			weap.add(w.getName());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,weap);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weaponSpinner.setAdapter(dataAdapter);
	}
	
	
	/**
	 * Returns the game info for this game.
	 * The gameCanvas must also be initialized in this method.
	 * 
	 * @return the GameInfo
	 */
	protected GameInfo makeGameInfo(){
		GameInfo g = new GameInfo(playerImages, playerNames, size.x, size.y, generator);
		g.setID(0);
		for (int i = 0; i < playerNames.length; i++){
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), g.getBunny(i).getImageResource());
			g.getBunny(i).setBitmapImage(bitmap);
		}
		gameCanvas = new GameCanvas(g, surfaceHolder);
		g.getBunny(0).setGameCanvas(gameCanvas);
		return g;
	}

	
	/**
	 * Sets up the buttons and stuff. Called first to set up the UI and class variables.
	 */
	protected void setup(){
		gameView = (SurfaceView) findViewById(R.id.game);
		surfaceHolder = gameView.getHolder();

		Intent intent = getIntent();
		aiDifficulty = intent.getIntExtra(AI_DIFFICULTY, 0);
		playerImages = intent.getIntArrayExtra(PLAYER_IMAGES);
		playerNames = intent.getStringArrayExtra(PLAYER_NAMES);
		terrainType = intent.getIntExtra(TERRAIN_TYPE, TERRAIN_TYPE_RANDOM);
		size = new android.graphics.Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		size.y = (int)(0.85 * size.y);
		
		switch (terrainType){
		case TERRAIN_TYPE_RANDOM: default:
			generator = new RandomGenerator();
			break;
		}
		
		angleText = (TextView) findViewById(R.id.angleText);	
		angleText.setTextColor(Color.parseColor("#000000"));
		angleText.setText("Angle: 90\u00B0");
		powerText = (TextView) findViewById(R.id.powText);
		powerText.setTextColor(Color.parseColor("#000000"));
		powerText.setText("Power: 0");
		
		power = (SeekBar) findViewById(R.id.seekBar1);		
		power.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){ 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                shotPower = progress;
                powerText.setText("Power: "+ shotPower);
            }
            public void onStartTrackingTouch(SeekBar seekBar){} 
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
		
		angle = (SeekBar) findViewById(R.id.seekBar2);		
		angle.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){ 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                shotAngle =  180 - progress;
                String temp = "";
                int temp2 = Math.abs(shotAngle - 90);
                if (shotAngle < 90){ temp = " Left" ; }
                if (shotAngle > 90){ temp = " Right" ; }
                angleText.setText("Angle: " + temp2 + "\u00B0" + temp);
                if (shotAngle == 90){ angleText.setText("Angle: UP" ); }
            }
            public void onStartTrackingTouch(SeekBar seekBar){} 
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
		shotAngle = 90;
		
		fire = (Button) findViewById(R.id.button1);	
		fire.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){ 
				firePressed();
			}
		});
		
		moveLeft = (Button) findViewById(R.id.buttonLeft);	
		moveLeft.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){
				moveBunny(game.getMyID(), true);
			}
		});
		
		moveRight = (Button) findViewById(R.id.buttonRight);	
		moveRight.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){ 
				moveBunny(game.getMyID(), false);
			}
		});
	}
}
